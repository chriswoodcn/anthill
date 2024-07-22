package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysRoleBo
import com.taotao.bmm.business.common.enum.SysUserType
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysRoleVo
import com.taotao.bmm.persistence.system.entity.QSysRoleEntity
import com.taotao.bmm.persistence.system.entity.SysRoleEntity
import com.taotao.bmm.persistence.system.entity.SysUserEntity
import com.taotao.bmm.persistence.system.repo.SysRoleRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysRoleService(
    private val sysRoleRepository: SysRoleRepository,
) : BaseService() {
    private val sysRoleEntity = QSysRoleEntity.sysRoleEntity

    fun buildExpression(bo: SysRoleBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysRoleEntity.id.eq(bo.id) }
            .exAnd(!bo.roleKey.isNullOrBlank()) { sysRoleEntity.roleKey.eq(bo.roleKey) }
            .exAnd(!bo.comId.isNullOrBlank()) { sysRoleEntity.comId.eq(bo.comId) }
            .exAnd(!bo.affiliateFlag.isNullOrBlank()) { sysRoleEntity.affiliateFlag.eq(bo.affiliateFlag) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysRoleEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysRoleEntity.roleNameJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysRoleEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysRoleEntity.createTime.loe(bo.queryEnd) }
    }

    /**
     * 分页列表
     */
    fun page(bo: SysRoleBo, page: PageForm): PageVo<SysRoleVo> {
        val builder = buildExpression(bo)
        builder.and(sysRoleEntity.status.ne(Constants.STATUS_2))
        val pageInstance = sysRoleRepository.findAll(builder, buildPageRequest(page))
        return buildPageVo(pageInstance)
    }

    /**
     * 创建和更新
     */
    fun save(bo: SysRoleBo): Int {
        val entity: SysRoleEntity = BeanUtil.copyBean(bo)
        preUpdateCheckStatus(bo.id, sysRoleRepository)
        val save = sysRoleRepository.save(entity)
        return save.id ?: 0
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysRoleRepository)
        return jpaQueryFactory.update(sysRoleEntity)
            .where(
                sysRoleEntity.id.`in`(ids.toList()),
                sysRoleEntity.status.ne(Constants.STATUS_3)
            )
            .set(sysRoleEntity.status, Constants.STATUS_2)
            .execute().toInt()
    }

    /**
     * 物理删除
     */
    @Transactional
    fun deletePhysic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysRoleRepository)
        return jpaQueryFactory.delete(sysRoleEntity)
            .where(
                sysRoleEntity.id.`in`(ids.toList()),
                sysRoleEntity.status.ne(Constants.STATUS_3)
            )
            .execute().toInt()
    }


    /**
     * 根据用户类型获取角色选择数据
     */
    fun getRoleSelectByUserType(flag: String, comId: String?): List<SysRoleVo> {
        val userEntity = AuthHelper.getAuthUser().instance as SysUserEntity
        when (userEntity.userType) {
            /**
             * 超管账户
             */
            SysUserType.SUPER_USER.code -> getSelectByCode(flag, comId)
            /**
             * 维护账户
             */
            SysUserType.MAINTAIN_USER.code -> {
                // 防止错误数据 flag只能是 "1" "2"
                val finalCode = if (flag.isBlank() || flag == SysUserType.SUPER_USER.code) {
                    SysUserType.MAINTAIN_USER.code
                } else {
                    flag
                }
                return if (Constants.FLAG_IS == userEntity.adminFlag) {
                    getSelectByCode(finalCode, comId)
                } else {
                    // 非管理员
                    if (flag == SysUserType.MAINTAIN_USER.code) {
                        val roles = AuthHelper.getAuthUser().roles.map { it.toInt() }.toSet()
                        val entities = sysRoleRepository.findAllById(roles.toList())
                        entities.map { BeanUtil.copyBean(it) }
                    } else {
                        getSelectByCode(finalCode, comId)
                    }

                }
            }

            /**
             * 客户账户
             */
            SysUserType.SYS_USER.code -> {
                val roles = AuthHelper.getAuthUser().roles.map { it.toInt() }.toSet()
                val entities = sysRoleRepository.findAllById(roles.toList())
                return entities.map { BeanUtil.copyBean(it) }
            }
        }
        return emptyList()
    }

    fun getSelectByCode(finalCode: String, comId: String?): List<SysRoleVo> {
        val roleEntities = getByAffiliateFlagAndComId(finalCode, comId)
        return roleEntities.map { BeanUtil.copyBean(it) }
    }

    fun getByAffiliateFlagAndComId(affiliateFlag: String, comId: String?): Iterable<SysRoleEntity> {
        val expression = sysRoleEntity.affiliateFlag.eq(affiliateFlag)
            .and(sysRoleEntity.status.`in`(setOf(Constants.STATUS_0, Constants.STATUS_3)))
        if (!comId.isNullOrBlank()) {
            expression.and(sysRoleEntity.comId.eq(comId))
        }
        val typedSort = Sort.sort(SysRoleEntity::class.java)
        val sort = typedSort.by(SysRoleEntity::roleSort).ascending()
        return sysRoleRepository.findAll(expression, sort)
    }

    /**
     * 查找模板
     */
    fun findTemplates(ids: Iterable<Int>): Iterable<SysRoleEntity> {
        val expression = sysRoleEntity.affiliateFlag.eq(Constants.AFFILIATE_FLAG_T)
            .and(sysRoleEntity.id.`in`(ids.toList()))
        return sysRoleRepository.findAll(expression)
    }
}