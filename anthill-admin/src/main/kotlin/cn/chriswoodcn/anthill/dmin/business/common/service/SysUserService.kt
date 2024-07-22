package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.dev33.satoken.secure.BCrypt
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysUserBo
import com.taotao.bmm.business.common.exception.BackendExceptionEnum
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysUserVo
import com.taotao.bmm.persistence.system.entity.QSysUserEntity
import com.taotao.bmm.persistence.system.entity.SysUserEntity
import com.taotao.bmm.persistence.system.repo.SysUserRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysUserService(
    private val sysUserRepository: SysUserRepository,
    private val sysLoginService: SysLoginService,
) : BaseService() {
    private val sysUserEntity = QSysUserEntity.sysUserEntity

    fun buildExpression(bo: SysUserBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysUserEntity.id.eq(bo.id) }
            .exAnd(!bo.username.isNullOrBlank()) { sysUserEntity.username.eq(bo.username) }
            .exAnd(!bo.nickname.isNullOrBlank()) { sysUserEntity.nickname.like(bo.nickname) }
            .exAnd(!bo.userType.isNullOrBlank()) { sysUserEntity.userType.eq(bo.userType) }
            .exAnd(!bo.email.isNullOrBlank()) { sysUserEntity.email.eq(bo.email) }
            .exAnd(!bo.mobile.isNullOrBlank()) { sysUserEntity.mobile.eq(bo.mobile) }
            .exAnd(!bo.sex.isNullOrBlank()) { sysUserEntity.sex.eq(bo.sex) }
            .exAnd(!bo.loginIp.isNullOrBlank()) { sysUserEntity.loginIp.eq(bo.loginIp) }
            .exAnd(!bo.comId.isNullOrBlank()) { sysUserEntity.comId.eq(bo.comId) }
            .exAnd(!bo.adminFlag.isNullOrBlank()) { sysUserEntity.adminFlag.eq(bo.adminFlag) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysUserEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysUserEntity.nickname.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysUserEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysUserEntity.createTime.loe(bo.queryEnd) }
    }

    fun page(bo: SysUserBo, page: PageForm): PageVo<SysUserVo> {
        val builder = buildExpression(bo)
        builder.and(sysUserEntity.status.ne(Constants.STATUS_2))
        val pageInstance = sysUserRepository.findAll(builder, buildPageRequest(page))
        return buildPageVo(pageInstance)
    }

    /**
     * 新增
     */
    fun add(bo: SysUserBo): Int {
        checkEmailUnique(bo.email!!, 0)
        bo.username = bo.email
        bo.password = BCrypt.hashpw(bo.password)
        if (bo.nickname == null)
            bo.nickname = RandomStringUtils.randomAlphanumeric(16)
        val entity: SysUserEntity = BeanUtil.copyBean(bo)
        val save = sysUserRepository.save(entity)
        return save.id ?: 0
    }

    /**
     * 校验邮箱是否唯一可用
     */
    fun checkEmailUnique(email: String, id: Int) {
        val expression = sysUserEntity.email.eq(email)
        if (id > 0) expression.and(sysUserEntity.id.ne(id))
        val count = sysUserRepository.count(expression)
        BackendExceptionEnum.USER_EMAIL_EXIST.stateTrue(count > 0)
    }

    /**
     * 更新
     */
    @Transactional
    fun update(bo: SysUserBo): Int {
        preUpdateCheckStatus(bo.id!!, sysUserRepository)
        //如果修改账号的邮箱需要校验
        if (!bo.email.isNullOrBlank()) {
            checkEmailUnique(bo.email!!, bo.id!!)
        }
        if (!bo.password.isNullOrBlank()) {
            bo.password = BCrypt.hashpw(bo.password)
        }
        val entity: SysUserEntity = BeanUtil.copyBean(bo)
        val save = sysUserRepository.save(entity)
        sysLoginService.refreshLoginUserCache(entity)
        return save.id ?: 0
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysUserRepository)
        return jpaQueryFactory.update(sysUserEntity)
            .where(
                sysUserEntity.id.`in`(ids.toList()),
                sysUserEntity.status.ne(Constants.STATUS_3)
            )
            .set(sysUserEntity.status, Constants.STATUS_2)
            .execute().toInt()
    }

}