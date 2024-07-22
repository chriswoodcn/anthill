package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysDictDataBo
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysDictDataVo
import com.taotao.bmm.persistence.system.entity.QSysDictDataEntity
import com.taotao.bmm.persistence.system.entity.SysDictDataEntity
import com.taotao.bmm.persistence.system.repo.SysDictDataRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SysDictDataService(
    private val sysDictDataRepository: SysDictDataRepository,
) : BaseService() {
    private val sysDictDataEntity = QSysDictDataEntity.sysDictDataEntity

    fun buildExpression(bo: SysDictDataBo): BooleanBuilder {
        return BooleanBuilder()
            .exAnd(null != bo.id) { sysDictDataEntity.id.eq(bo.id) }
            .exAnd(!bo.dictType.isNullOrBlank()) { sysDictDataEntity.dictType.eq(bo.dictType) }
            .exAnd(!bo.dictValue.isNullOrBlank()) { sysDictDataEntity.dictValue.eq(bo.dictValue) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysDictDataEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysDictDataEntity.dictLabelJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysDictDataEntity.createTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysDictDataEntity.createTime.loe(bo.queryEnd) }
    }

    /**
     * 列表
     */
    fun list(bo: SysDictDataBo): List<SysDictDataVo> {
        val builder = buildExpression(bo)
        builder.and(sysDictDataEntity.status.ne(Constants.STATUS_2))
        val all = sysDictDataRepository.findAll(builder)
        return all.map { BeanUtil.copyBean(it) }
    }

    /**
     * 根据字典类型获取字典数据
     */
    fun listByDictType(dictType: String): List<SysDictDataVo> {
        val builder = sysDictDataEntity.dictType.eq(dictType)
        builder.and(sysDictDataEntity.status.ne(Constants.STATUS_2))
        val all = sysDictDataRepository.findAll(builder)
        return all.map { BeanUtil.copyBean(it) }
    }


    /**
     * 分页列表
     */
    fun page(bo: SysDictDataBo, page: PageForm): PageVo<SysDictDataVo> {
        val builder = buildExpression(bo)
        builder.and(sysDictDataEntity.status.ne(Constants.STATUS_2))
        val typedSort = Sort.sort(SysDictDataEntity::class.java)
        val sort = typedSort.by(SysDictDataEntity::id).ascending()
            .and(typedSort.by(SysDictDataEntity::dictSort).ascending())
        val pageRequest = buildPageRequest(page, sort)
        val pageInstance = sysDictDataRepository.findAll(builder, pageRequest)
        return buildPageVo(pageInstance)
    }

    /**
     * 创建和更新
     */
    fun save(bo: SysDictDataBo): Int {
        val entity: SysDictDataEntity = BeanUtil.copyBean(bo)
        preUpdateCheckStatus(bo.id, sysDictDataRepository)
        val save = sysDictDataRepository.save(entity)
        return save.id ?: 0
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<Int>): Int {
        return jpaQueryFactory.update(sysDictDataEntity).where(
            sysDictDataEntity.id.`in`(ids.toList()), sysDictDataEntity.status.ne(Constants.STATUS_3)
        ).set(sysDictDataEntity.status, Constants.STATUS_2).execute().toInt()
    }

    /**
     * 物理删除
     */
    @Transactional
    fun deletePhysic(ids: Array<Int>): Int {
        preDeleteCheckStatus(ids, sysDictDataRepository)
        return jpaQueryFactory.delete(sysDictDataEntity)
            .where(
                sysDictDataEntity.id.`in`(ids.toList()),
                sysDictDataEntity.status.ne(Constants.STATUS_3)
            )
            .execute().toInt()
    }
}