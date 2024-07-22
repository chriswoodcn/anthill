package com.taotao.bmm.support

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE
import org.hibernate.annotations.QueryHints.READ_ONLY
import java.util.stream.Stream

/**
 * 扩展 BooleanBuilder
 */
fun BooleanBuilder.exAnd(bool: Boolean, right: () -> Predicate?): BooleanBuilder {
    if (bool) this.and(right())
    return this
}

fun BooleanBuilder.exAndAnyOf(bool: Boolean, right: () -> Predicate?): BooleanBuilder {
    if (bool) this.andAnyOf(right())
    return this
}

fun BooleanBuilder.exAndNot(bool: Boolean, right: () -> Predicate?): BooleanBuilder {
    if (bool) this.andNot(right())
    return this
}

fun BooleanBuilder.exOr(bool: Boolean, right: () -> Predicate?): BooleanBuilder {
    if (bool) this.or(right())
    return this
}

fun BooleanBuilder.exOrAllOf(bool: Boolean, right: () -> Predicate?): BooleanBuilder {
    if (bool) this.orAllOf(right())
    return this
}