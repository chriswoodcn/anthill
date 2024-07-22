package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.redis.utils.RedisCacheUtil
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysConfigBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysConfigVo
import com.taotao.bmm.persistence.system.entity.QSysConfigEntity
import com.taotao.bmm.persistence.system.entity.SysConfigEntity
import com.taotao.bmm.persistence.system.repo.SysConfigRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysConfigService(
    private val sysConfigRepository: SysConfigRepository
) : BaseService() {
    private val sysConfigEntity = QSysConfigEntity.sysConfigEntity

    fun buildExpression(bo: SysConfigBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysConfigEntity.id.eq(bo.id) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysConfigEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysConfigEntity.configNameJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysConfigEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysConfigEntity.createTime.loe(bo.queryEnd) }
    }

    /**
     * 列表
     */
    fun list(bo: SysConfigBo): List<SysConfigVo> {
        val builder = buildExpression(bo)
        builder.and(sysConfigEntity.status.ne(Constants.STATUS_2))
        val all = sysConfigRepository.findAll(builder)
        return all.map { BeanUtil.copyBean(it) }
    }

    /**
     * 分页列表
     */
    fun page(bo: SysConfigBo, page: PageForm): PageVo<SysConfigVo> {
        val builder = buildExpression(bo)
        builder.and(sysConfigEntity.status.ne(Constants.STATUS_2))
        val pageInstance = sysConfigRepository.findAll(builder, buildPageRequest(page))
        return buildPageVo(pageInstance)
    }

    /**
     * 创建和更新
     */
    fun save(bo: SysConfigBo): Int {
        val entity: SysConfigEntity = BeanUtil.copyBean(bo)
        preUpdateCheckStatus(bo.id, sysConfigRepository)
        val save = sysConfigRepository.save(entity)
        return save.id ?: 0
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        return jpaQueryFactory.update(sysConfigEntity).where(
            sysConfigEntity.id.`in`(ids.toList()), sysConfigEntity.status.ne(Constants.STATUS_3)
        ).set(sysConfigEntity.status, Constants.STATUS_2).execute().toInt()
    }

    /**
     * 物理删除
     */
    @Transactional
    fun deletePhysic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysConfigRepository)
        return jpaQueryFactory.delete(sysConfigEntity)
            .where(
                sysConfigEntity.id.`in`(ids.toList()),
                sysConfigEntity.status.ne(Constants.STATUS_3)
            )
            .execute().toInt()
    }

    /**
     * 清除缓存
     */
    fun clearCache(key: String? = null) {
        if (key.isNullOrBlank()) {
            RedisCacheUtil.clear(Constants.CACHE_CONFIG)
        } else {
            RedisCacheUtil.evict(Constants.CACHE_CONFIG, key)
        }
    }

    /**
     * 加载缓存
     */
    fun loadCache(key: String? = null) {
        val bo = SysConfigBo::class.java.getDeclaredConstructor().newInstance()
        if (key.isNullOrBlank()) {
            val list = list(bo)
            list.parallelStream().forEach {
                if (!it.configKey.isNullOrBlank())
                    RedisCacheUtil.put(Constants.CACHE_CONFIG, it.configKey!!, it)
            }
        } else {
            bo.configKey = key
            val list = list(bo)
            if (list.isNotEmpty())
                RedisCacheUtil.put(Constants.CACHE_CONFIG, key, list[0])
        }
    }

    /**
     * 刷新缓存
     */
    fun refreshCache(key: String? = null) {
        clearCache(key)
        loadCache(key)
    }

    /**
     * 获取系统参数
     */
    fun get(key: String): SysConfigVo? {
        return RedisCacheUtil[Constants.CACHE_CONFIG, key]
    }
}