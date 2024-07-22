package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysMenuBo
import com.taotao.bmm.business.common.enum.SysUserType
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysMenuVo
import com.taotao.bmm.persistence.system.entity.*
import com.taotao.bmm.persistence.system.repo.SysMenuRepository
import com.taotao.bmm.persistence.system.repo.SysRoleMenuRefRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysMenuService(
    private val sysMenuRepository: SysMenuRepository,
    private val sysRoleMenuRefRepository: SysRoleMenuRefRepository,
) : BaseService() {
    private val sysMenuEntity = QSysMenuEntity.sysMenuEntity
    private val sysRoleMenuRef = QSysRoleMenuRef.sysRoleMenuRef

    fun buildExpression(bo: SysMenuBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysMenuEntity.id.eq(bo.id) }
            .exAnd(null != bo.parentId) { sysMenuEntity.parentId.eq(bo.parentId) }
            .exAnd(!bo.path.isNullOrBlank()) { sysMenuEntity.path.eq(bo.path) }
            .exAnd(!bo.component.isNullOrBlank()) { sysMenuEntity.component.eq(bo.component) }
            .exAnd(!bo.menuType.isNullOrBlank()) { sysMenuEntity.menuType.eq(bo.menuType) }
            .exAnd(!bo.perms.isNullOrBlank()) { sysMenuEntity.perms.eq(bo.perms) }
            .exAnd(!bo.icon.isNullOrBlank()) { sysMenuEntity.icon.eq(bo.icon) }
            .exAnd(!bo.menuVersion.isNullOrBlank()) { sysMenuEntity.menuVersion.eq(bo.menuVersion) }
            .exAnd(!bo.hiddenFlag.isNullOrBlank()) { sysMenuEntity.hiddenFlag.eq(bo.hiddenFlag) }
            .exAnd(!bo.frameFlag.isNullOrBlank()) { sysMenuEntity.frameFlag.eq(bo.frameFlag) }
            .exAnd(!bo.affiliateFlag.isNullOrBlank()) { sysMenuEntity.affiliateFlag.eq(bo.affiliateFlag) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysMenuEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysMenuEntity.menuNameJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysMenuEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysMenuEntity.createTime.loe(bo.queryEnd) }
    }

    /**
     * 根据用户类型获取菜单选择数据
     */
    fun getMenuSelectByUserType(flag: String): List<SysMenuVo> {
        val userEntity = AuthHelper.getAuthUser().instance as SysUserEntity
        when (userEntity.userType) {
            /**
             * 超管账户获取所有属于超管的菜单 或者获取所有维护菜单 或者获取所有客户菜单
             */
            SysUserType.SUPER_USER.code -> getSelectByCode(flag)
            /**
             * 维护账户根据自己角色获取关联菜单 或者获取所有客户菜单
             */
            SysUserType.MAINTAIN_USER.code -> {
                // 防止错误数据 flag只能是 "1" "2"
                val finalCode = if (flag.isBlank() || flag == SysUserType.SUPER_USER.code) {
                    SysUserType.MAINTAIN_USER.code
                } else {
                    flag
                }
                return if (Constants.FLAG_IS == userEntity.adminFlag) {
                    getSelectByCode(finalCode)
                } else {
                    // 非管理员
                    if (flag == SysUserType.MAINTAIN_USER.code) {
                        val roles = AuthHelper.getAuthUser().roles.map { it.toInt() }.toSet()
                        val menusByRoles = getMenusByRoles(roles).toList()
                        genMenuTreeSelect(menusByRoles)
                    } else {
                        getSelectByCode(finalCode)
                    }
                }
            }
            /**
             * 客户账户根据自己角色获取关联菜单
             */
            SysUserType.SYS_USER.code -> {
                val roles = AuthHelper.getAuthUser().roles.map { it.toInt() }.toSet()
                getSelectByRoles(roles)
            }
        }
        return emptyList()
    }

    /**
     * 根据affiliateFlag获取菜单选择数据
     */
    fun getSelectByCode(finalCode: String): List<SysMenuVo> {
        val menuEntities = getByAffiliateFlag(finalCode)
        val realMenuEntities = menuEntities
            .filter { Constants.FLAG_NOT == it.hiddenFlag }
        return genMenuTreeSelect(realMenuEntities)
    }

    fun getByAffiliateFlag(affiliateFlag: String): Iterable<SysMenuEntity> {
        val expression = sysMenuEntity.affiliateFlag.eq(affiliateFlag)
            .and(sysMenuEntity.status.`in`(setOf(Constants.STATUS_0, Constants.STATUS_3)))
        val typedSort = Sort.sort(SysMenuEntity::class.java)
        val sort = typedSort.by(SysMenuEntity::parentId).ascending()
            .and(typedSort.by(SysMenuEntity::orderNum).ascending())
        return sysMenuRepository.findAll(expression, sort)
    }

    /**
     * 根据角色组获取菜单选择数据
     */
    fun getSelectByRoles(roles: Set<Int>): List<SysMenuVo> {
        val menusByRoles = getMenusByRoles(roles).toList()
        return genMenuTreeSelect(menusByRoles)
    }

    /**
     * 生成树状数据结构
     */
    fun genMenuTreeSelect(list: Iterable<SysMenuEntity>): List<SysMenuVo> {
        val menuVos: List<SysMenuVo> = list.map { BeanUtil.copyBean(it) }
        return genMenuTreeSelectChildren(0, menuVos)
    }

    /**
     * 递归设置children
     */
    fun genMenuTreeSelectChildren(pId: Int, pool: List<SysMenuVo>): List<SysMenuVo> {
        val menus = pool.filter { it.parentId == pId }
        if (menus.isEmpty()) {
            return emptyList()
        } else {
            menus.forEach {
                it.children = genMenuTreeSelectChildren(it.id, pool)
            }
        }
        return menus
    }

    /**
     * 根据用户角色组获取关联菜单
     */
    fun getMenusByRoles(roles: Set<Int>): Set<SysMenuEntity> {
        val roleMenuRefs = mutableSetOf<SysRoleMenuRef>()
        roles.forEach {
            val expression = sysRoleMenuRef.id.roleId.eq(it)
            val result = sysRoleMenuRefRepository.findAll(expression)
            roleMenuRefs.addAll(result)
        }
        return roleMenuRefs.mapNotNull { it.menu }.toSet()
    }

    fun list(bo: SysMenuBo): List<SysMenuVo> {
        val builder = buildExpression(bo)
        builder.and(sysMenuEntity.status.ne(Constants.STATUS_2))
        val menus = sysMenuRepository.findAll(builder)
        return genMenuTreeSelect(menus)
    }

    fun page(bo: SysMenuBo, page: PageForm): PageVo<SysMenuVo> {
        val builder = buildExpression(bo)
        builder.and(sysMenuEntity.status.ne(Constants.STATUS_2))
        val typedSort = Sort.sort(SysMenuEntity::class.java)
        val sort = typedSort.by(SysMenuEntity::parentId).ascending()
            .and(typedSort.by(SysMenuEntity::orderNum).ascending())
        val pageRequest = buildPageRequest(page, sort)
        val pageInstance = sysMenuRepository.findAll(builder, pageRequest)
        return buildPageVo(pageInstance)
    }

    /**
     * 创建和更新
     */
    fun save(bo: SysMenuBo): Int {
        val entity: SysMenuEntity = BeanUtil.copyBean(bo)
        preUpdateCheckStatus(bo.id, sysMenuRepository)
        val save = sysMenuRepository.save(entity)
        return save.id ?: 0
    }


    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysMenuRepository)
        return jpaQueryFactory.update(sysMenuEntity)
            .where(
                sysMenuEntity.id.`in`(ids.toList()),
                sysMenuEntity.status.ne(Constants.STATUS_3)
            )
            .set(sysMenuEntity.status, Constants.STATUS_2)
            .execute().toInt()
    }

    /**
     * 物理删除
     */
    @Transactional
    fun deletePhysic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysMenuRepository)
        return jpaQueryFactory.delete(sysMenuEntity)
            .where(
                sysMenuEntity.id.`in`(ids.toList()),
                sysMenuEntity.status.ne(Constants.STATUS_3)
            )
            .execute().toInt()
    }
}