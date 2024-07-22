package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.redis.utils.RedisCacheUtil
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysDictTypeBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysDictDataVo
import com.taotao.bmm.business.common.vo.SysDictTypeVo
import com.taotao.bmm.persistence.system.entity.QSysDictTypeEntity
import com.taotao.bmm.persistence.system.entity.SysDictTypeEntity
import com.taotao.bmm.persistence.system.repo.SysDictTypeRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysDictTypeService(
    private val sysDictTypeRepository: SysDictTypeRepository,
    private val sysDictDataService: SysDictDataService,
) : BaseService() {
    private val sysDictTypeEntity = QSysDictTypeEntity.sysDictTypeEntity

    fun buildExpression(bo: SysDictTypeBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysDictTypeEntity.id.eq(bo.id) }
            .exAnd(!bo.dictType.isNullOrBlank()) { sysDictTypeEntity.dictType.eq(bo.dictType) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysDictTypeEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysDictTypeEntity.dictNameJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysDictTypeEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysDictTypeEntity.createTime.loe(bo.queryEnd) }
    }

    /**
     * 列表
     */
    fun list(bo: SysDictTypeBo): List<SysDictTypeVo> {
        val builder = buildExpression(bo)
        builder.and(sysDictTypeEntity.status.ne(Constants.STATUS_2))
        val all = sysDictTypeRepository.findAll(builder)
        return all.map { BeanUtil.copyBean(it) }
    }

    /**
     * 分页列表
     */
    fun page(bo: SysDictTypeBo, page: PageForm): PageVo<SysDictTypeVo> {
        val builder = buildExpression(bo)
        builder.and(sysDictTypeEntity.status.ne(Constants.STATUS_2))
        val typedSort = Sort.sort(SysDictTypeEntity::class.java)
        val sort = typedSort.by(SysDictTypeEntity::id).ascending()
        val pageRequest = buildPageRequest(page, sort)
        val pageInstance = sysDictTypeRepository.findAll(builder, pageRequest)
        return buildPageVo(pageInstance)
    }

    /**
     * 创建和更新
     */
    fun save(bo: SysDictTypeBo): Int {
        val entity: SysDictTypeEntity = BeanUtil.copyBean(bo)
        preUpdateCheckStatus(bo.id, sysDictTypeRepository)
        val save = sysDictTypeRepository.save(entity)
        return save.id ?: 0
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        return jpaQueryFactory.update(sysDictTypeEntity).where(
            sysDictTypeEntity.id.`in`(ids.toList()), sysDictTypeEntity.status.ne(Constants.STATUS_3)
        ).set(sysDictTypeEntity.status, Constants.STATUS_2).execute().toInt()
    }

    /**
     * 物理删除
     */
    @Transactional
    fun deletePhysic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysDictTypeRepository)
        return jpaQueryFactory.delete(sysDictTypeEntity)
            .where(
                sysDictTypeEntity.id.`in`(ids.toList()),
                sysDictTypeEntity.status.ne(Constants.STATUS_3)
            )
            .execute().toInt()
    }

    /**
     * 清除缓存
     */
    fun clearCache(key: String? = null) {
        if (key.isNullOrBlank()) {
            RedisCacheUtil.clear(Constants.CACHE_DICT)
        } else {
            RedisCacheUtil.evict(Constants.CACHE_DICT, key)
        }
    }

    /**
     * 加载缓存
     */
    fun loadCache(key: String? = null) {
        val bo = SysDictTypeBo::class.java.getDeclaredConstructor().newInstance()
        if (key.isNullOrBlank()) {
            val list = list(bo)
            list.parallelStream().forEach {
                if (!it.dictType.isNullOrBlank()) {
                    val dataVoList = sysDictDataService.listByDictType(it.dictType!!)
                    RedisCacheUtil.put(Constants.CACHE_DICT, it.dictType!!, dataVoList)
                }
            }
        } else {
            bo.dictType = key
            val dataVoList = sysDictDataService.listByDictType(key)
            if (dataVoList.isNotEmpty())
                RedisCacheUtil.put(Constants.CACHE_DICT, key, dataVoList)
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
     * 获取字典数据
     */
    fun get(key: String): List<SysDictDataVo> {
        return RedisCacheUtil[Constants.CACHE_DICT, key] ?: emptyList()
    }
}