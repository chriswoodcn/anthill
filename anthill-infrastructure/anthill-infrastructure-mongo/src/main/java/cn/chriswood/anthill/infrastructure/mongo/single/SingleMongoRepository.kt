package cn.chriswood.anthill.infrastructure.mongo.single

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository
import org.springframework.data.repository.query.FluentQuery
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.function.Function

/**
 * SingleMongoRepository
 * 单数据源的repositoryBaseClass
 */
open class SingleMongoRepository<T, ID>(
    private val entityInformation: MongoEntityInformation<T, ID>,
    @Qualifier("singleMongoTemplate")
    private val mongoTemplate: MongoTemplate,
) : MongoRepository<T, ID> {

    private var delegate: SimpleMongoRepository<T, ID> =
        SimpleMongoRepository(entityInformation, mongoTemplate)

    override fun <S : T> save(entity: S & Any): S & Any {
        return delegate.save(entity)
    }

    override fun <S : T> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return delegate.saveAll(entities)
    }

    override fun findById(id: ID & Any): Optional<T> {
        return delegate.findById(id)
    }

    override fun existsById(id: ID & Any): Boolean {
        return delegate.existsById(id)
    }

    override fun <S : T> findAll(example: Example<S>): MutableList<S> {
        return delegate.findAll(example)
    }

    override fun <S : T> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return delegate.findAll(example)
    }

    override fun findAll(): MutableList<T> {
        return delegate.findAll()
    }

    override fun findAll(sort: Sort): MutableList<T> {
        return delegate.findAll(sort)
    }

    override fun findAll(pageable: Pageable): Page<T> {
        return delegate.findAll(pageable)
    }

    override fun count(): Long {
        return delegate.count()
    }

    override fun deleteAll() {
        return delegate.deleteAll()
    }

    override fun <S : T> insert(entities: MutableIterable<S>): MutableList<S> {
        return delegate.insert(entities)
    }

    override fun <S : T> insert(entity: S & Any): S & Any {
        return delegate.insert(entity)
    }

    override fun <S : T, R : Any?> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R & Any {
        return delegate.findBy(example, queryFunction)
    }

    override fun <S : T> exists(example: Example<S>): Boolean {
        return delegate.exists(example)
    }

    override fun <S : T> findOne(example: Example<S>): Optional<S> {
        return delegate.findOne(example)
    }

    override fun deleteAll(entities: MutableIterable<T>) {
        return delegate.deleteAll(entities)
    }

    override fun deleteAllById(ids: MutableIterable<ID>) {
        return delegate.deleteAllById(ids)
    }

    override fun delete(entity: T & Any) {
        return delegate.delete(entity)
    }

    override fun deleteById(id: ID & Any) {
        return delegate.deleteById(id)
    }

    override fun <S : T> count(example: Example<S>): Long {
        return delegate.count(example)
    }

    override fun findAllById(ids: MutableIterable<ID>): MutableList<T> {
        return delegate.findAllById(ids)
    }

    override fun <S : T> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return delegate.findAll(example, pageable)
    }

    open fun <R> aggregate(aggregation: Aggregation, outputType: Class<R>): List<R> {
        return mongoTemplate
            .aggregate(aggregation, getEntityClass(), outputType)
            .mappedResults
    }

    open fun getEntityClass(): Class<T> {
        @Suppress("UNCHECKED_CAST")
        return (javaClass.getGenericSuperclass() as ParameterizedType).actualTypeArguments[0] as Class<T>
    }
}
