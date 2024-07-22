package com.taotao.bmm.business.common.service

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import com.querydsl.jpa.impl.JPAQueryFactory
import com.taotao.bmm.business.common.exception.BackendExceptionEnum
import com.taotao.bmm.business.common.form.PageForm
import com.taotao.bmm.business.common.vo.PageVo
import com.taotao.bmm.support.Constants
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

open class BaseService {
    @Autowired
    lateinit var jpaQueryFactory: JPAQueryFactory

    val log: Logger = LoggerFactory.getLogger(javaClass)
    inline fun <reified T> buildPageVo(page: Page<*>? = null): PageVo<T> {
        if (null == page) return PageVo(0, 1, 10, 0, null)
        val convertContent: List<T> = page.content.map { BeanUtil.copyBean(it) }
        return PageVo(
            page.totalElements,
            page.size,
            page.number + 1,
            page.totalPages,
            convertContent
        )
    }

    fun buildPageRequest(pageNum: Int = 0, pageSize: Int = 10): PageRequest {
        return PageRequest.of(pageNum, pageSize)
    }

    fun buildPageRequest(pageNum: Int = 0, pageSize: Int = 10, sort: Sort): PageRequest {
        return PageRequest.of(pageNum, pageSize, sort)
    }

    fun buildPageRequest(form: PageForm): PageRequest {
        return buildPageRequest(form.pageNum - 1, form.pageSize)
    }

    fun buildPageRequest(form: PageForm, sort: Sort): PageRequest {
        return buildPageRequest(form.pageNum - 1, form.pageSize, sort)
    }

    /**
     * preUpdateCheckStatus 保存前检查是否是系统内置数据
     * 内置数据无法修改
     */
    inline fun <reified T> preUpdateCheckStatus(id: Any?, repository: JpaSpecificationExecutor<T>) {
        if (id != null) {
            val specification = Specification { root: Root<T>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
                cb.equal(root.get<Any>("id"), id)
                cb.equal(root.get<Any>("status"), Constants.STATUS_3)
            }
            val list = repository.findAll(specification)
            BackendExceptionEnum.NOT_ALLOWED_EDIT.stateTrue(list.isEmpty())
        }
    }

    /**
     * preDeleteCheckStatus 删除前检查是否是系统内置数据
     * 内置数据无法删除
     */
    inline fun <reified T> preDeleteCheckStatus(ids: Array<*>?, repository: JpaSpecificationExecutor<T>) {
        if (!ids.isNullOrEmpty()) {
            val specification = Specification { root: Root<T>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
                val inClause = cb.`in`(root.get<Any>("id"))
                ids.forEach {
                    inClause.value(it)
                }
                cb.equal(root.get<Any>("status"), Constants.STATUS_3)
            }
            val list = repository.findAll(specification)
            BackendExceptionEnum.NOT_ALLOWED_EDIT.stateTrue(list.isEmpty())
        }
    }

}