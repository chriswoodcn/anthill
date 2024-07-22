package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil.objectMapper
import com.querydsl.core.BooleanBuilder
import com.taotao.bmm.business.common.bo.SysCompanyBo
import com.taotao.bmm.business.common.exception.BackendExceptionEnum
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.business.common.vo.SysCompanyVo
import com.taotao.bmm.persistence.system.entity.QSysCompanyEntity
import com.taotao.bmm.persistence.system.entity.SysCompanyEntity
import com.taotao.bmm.persistence.system.repo.SysCompanyRepository
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.exAnd
import jakarta.persistence.EntityManager
import org.springframework.data.repository.query.FluentQuery
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.OutputStream


@Service
class SysCompanyService(
    private val sysCompanyRepository: SysCompanyRepository,
    private val sysRoleService: SysRoleService,
    private val entityManager: EntityManager,
) : BaseService() {
    private val sysCompanyEntity = QSysCompanyEntity.sysCompanyEntity
    fun buildExpression(bo: SysCompanyBo): BooleanBuilder {
        return BooleanBuilder().exAnd(!bo.id.isNullOrBlank()) { sysCompanyEntity.id.eq(bo.id) }
            .exAnd(null != bo.templateId) { sysCompanyEntity.templateId.eq(bo.templateId) }
            .exAnd(null != bo.activeTime) { sysCompanyEntity.activeTime.loe(bo.activeTime) }
            .exAnd(!bo.status.isNullOrBlank() && Constants.STATUS_2 != bo.status)
            { sysCompanyEntity.status.eq(bo.status) }
            .exAnd(null != bo.queryName) { sysCompanyEntity.companyNameJson.like(bo.queryName) }
            .exAnd(null != bo.queryStart) { sysCompanyEntity.activeTime.goe(bo.queryStart) }
            .exAnd(null != bo.queryEnd) { sysCompanyEntity.activeTime.loe(bo.queryEnd) }
    }

    fun getSelect(): List<SysCompanyVo> {
        val expression = sysCompanyEntity.status.`in`(Constants.STATUS_0, Constants.STATUS_3)
        val all = sysCompanyRepository.findAll(expression)
        return all.map { BeanUtil.copyBean(it) }
    }

    fun genSysCompanyVo(list: Iterable<SysCompanyEntity>): List<SysCompanyVo> {
        val templateIds = list.mapNotNull { it.templateId }.toSet()
        val templates = sysRoleService.findTemplates(templateIds)
        return list.map {
            val vo = BeanUtil.copyBean<SysCompanyVo>(it)
            if (null != vo.templateId) {
                val filter = templates.filter { t -> t.id == vo.templateId }
                if (filter.isNotEmpty()) vo.templateNameJson = filter[0].roleNameJson
            }
            vo
        }
    }

    @Transactional(readOnly = true)
    fun export(bo: SysCompanyBo, outputStream: OutputStream) {
        val builder = buildExpression(bo)
        builder.and(sysCompanyEntity.status.ne(Constants.STATUS_2))

        val streamData =
            sysCompanyRepository.findBy(builder, FluentQuery.FetchableFluentQuery<SysCompanyEntity>::stream)

        val jsonGenerator = objectMapper.factory
            .createGenerator(outputStream)
        try {
            jsonGenerator.writeStartArray()
            val iterable = streamData.iterator()
            while (iterable.hasNext()) {
                val entity = iterable.next()
                entityManager.detach(entity)
                val vo = BeanUtil.copyBean<SysCompanyVo>(entity)
                jsonGenerator.writeObject(vo)
            }
            jsonGenerator.writeEndArray()
        } catch (_: Exception) {

        } finally {
            streamData.close()
            jsonGenerator.close()
        }
    }

    fun list(bo: SysCompanyBo): List<SysCompanyVo> {
        val builder = buildExpression(bo)
        builder.and(sysCompanyEntity.status.ne(Constants.STATUS_2))
        val companies = sysCompanyRepository.findAll(builder)
        return genSysCompanyVo(companies)
    }

    fun page(bo: SysCompanyBo, page: PageForm): PageVo<SysCompanyVo> {
        val builder = buildExpression(bo)
        builder.and(sysCompanyEntity.status.ne(Constants.STATUS_2))
        val pageInstance = sysCompanyRepository.findAll(builder, buildPageRequest(page))
        return buildPageVo(pageInstance)
    }

    /**
     * 创建
     */
    fun add(bo: SysCompanyBo): String? {
        val entity: SysCompanyEntity = BeanUtil.copyBean(bo)
        checkIdUnique(bo.id!!)
        val save = sysCompanyRepository.save(entity)
        return save.id
    }

    /**
     * 更新
     */
    fun update(bo: SysCompanyBo): String? {
        val entity: SysCompanyEntity = BeanUtil.copyBean(bo)
        val save = sysCompanyRepository.save(entity)
        return save.id
    }

    fun checkIdUnique(id: String) {
        val expression = sysCompanyEntity.id.eq(id)
        val count = sysCompanyRepository.count(expression)
        BackendExceptionEnum.COMPANY_ID_EXIST.stateTrue(count > 0)
    }

    /**
     * 逻辑删除
     */
    @Transactional
    fun deleteLogic(ids: Array<String>): Int {
        return jpaQueryFactory.update(sysCompanyEntity).where(
            sysCompanyEntity.id.`in`(ids.toList()), sysCompanyEntity.status.ne(Constants.STATUS_3)
        ).set(sysCompanyEntity.status, Constants.STATUS_2).execute().toInt()
    }
}