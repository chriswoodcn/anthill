package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import cn.dev33.satoken.secure.BCrypt
import cn.dev33.satoken.stp.SaLoginModel
import com.taotao.bmm.business.common.bo.AuthUserBo
import com.taotao.bmm.business.common.enum.SysUserType
import com.taotao.bmm.business.common.exception.BackendExceptionEnum
import com.taotao.bmm.business.common.form.BackendLoginForm
import com.taotao.bmm.business.common.vo.BackendLoginVo
import com.taotao.bmm.business.common.vo.SysMenuVo
import com.taotao.bmm.persistence.system.entity.QSysUserEntity
import com.taotao.bmm.persistence.system.entity.QSysUserRoleRel
import com.taotao.bmm.persistence.system.entity.SysUserEntity
import com.taotao.bmm.persistence.system.repo.SysUserRepository
import com.taotao.bmm.persistence.system.repo.SysUserRoleRefRepository
import com.taotao.bmm.support.Constants
import org.springframework.stereotype.Service

@Service
class SysLoginService(
    private val sysUserRepository: SysUserRepository,
    private val sysUserRoleRefRepository: SysUserRoleRefRepository,
    private val sysMenuService: SysMenuService,
) {
    private val sysUserEntity = QSysUserEntity.sysUserEntity
    private val sysUserRoleRel = QSysUserRoleRel.sysUserRoleRel
    fun login(form: BackendLoginForm): BackendLoginVo {
        val predicate = sysUserEntity.username.eq(form.username)
            .and(sysUserEntity.status.`in`(listOf(Constants.STATUS_0, Constants.STATUS_3)))
        val optional = sysUserRepository.findOne(predicate)
        BackendExceptionEnum.USER_NOT_EXIST.stateTrue(optional.isPresent)
        val userEntity = optional.get()
        BackendExceptionEnum.USER_FROZEN.stateTrue(
            Constants.STATUS_1 != userEntity.status
        )
        BackendExceptionEnum.USER_PASSWORD_ERR.stateTrue(
            BCrypt.checkpw(form.password, userEntity.password)
        )
        val genRoles = genRoles(userEntity)
        val genPermissions = if (Constants.FLAG_IS == userEntity.adminFlag) {
            setOf(Constants.PERMISSION_ADMIN)
        } else genPermissions(genRoles)
        return BackendLoginVo(
            genToken(userEntity),
            BeanUtil.copyBean(userEntity),
            genRoles, genPermissions
        )
    }

    fun userInfo(): BackendLoginVo {
        val authUser = AuthHelper.getTypedAuthUser<SysUserEntity>()
        return BackendLoginVo(
            null,
            BeanUtil.copyBean(authUser.instance),
            authUser.roles.map { it.toInt() }.toSet(),
            authUser.permissions
        )
    }

    fun genAuthUserBo(userEntity: SysUserEntity): AuthUserBo<SysUserEntity> {
        val originRoles = genRoles(userEntity)
        val permissions = genPermissions(originRoles)
        return AuthUserBo(
            userEntity,
            userEntity.id.toString(),
            SysUserType.SUPER_USER.code,
            userEntity.username,
            permissions,
            originRoles.map { it.toString() }.toSet(),
        )
    }

    fun genToken(userEntity: SysUserEntity): String {
        val saLoginModel = SaLoginModel()
            .setIsWriteHeader(false)
        AuthHelper.login(genAuthUserBo(userEntity), saLoginModel)
        return AuthHelper.getToken()
    }

    fun genRoles(userEntity: SysUserEntity): Set<Int> {
        val expression = sysUserRoleRel.id.userId.eq(userEntity.id!!)
        val userRoleRels = sysUserRoleRefRepository.findAll(expression)
        return userRoleRels.map { it.id.roleId }.toSet()
    }


    fun genPermissions(roles: Set<Int>): Set<String> {
        if (roles.isEmpty()) return emptySet()
        val menus = sysMenuService.getMenusByRoles(roles)
        return if (menus.isEmpty()) {
            emptySet()
        } else {
            menus.mapNotNull { it.perms }.toSet()
        }
    }

    fun getRouters(): List<SysMenuVo> {
        val userId = AuthHelper.getUserId().toInt()
        val option = sysUserRepository.findById(userId)
        if (option.isEmpty) return emptyList()
        genRoutersByUser(option.get())
        return genRoutersByUser(option.get())
    }

    fun genRoutersByUser(userEntity: SysUserEntity): List<SysMenuVo> {
        val adminFlag = userEntity.adminFlag
        val userTypeCode = userEntity.userType ?: Constants.AFFILIATE_FLAG_0
        return if (Constants.FLAG_IS == adminFlag) {
            val menuList = sysMenuService.getByAffiliateFlag(userTypeCode)
            menuList.map { BeanUtil.copyBean(it) }
        } else {
            val roles = genRoles(userEntity)
            val menuEntities = sysMenuService.getMenusByRoles(roles)
            menuEntities.toList().map { BeanUtil.copyBean(it) }
        }
    }

    /**
     * 刷新用户登录缓存数据
     */
    fun refreshLoginUserCache(userEntity: SysUserEntity) {
        if (AuthHelper.isLogin("${SysUserType.SUPER_USER.code}:${userEntity.id}")) {
            val authUserBo = genAuthUserBo(userEntity)
            val loginId = authUserBo.getAuthLabel()
            AuthHelper.refreshUser(loginId, authUserBo)
        }
    }
}