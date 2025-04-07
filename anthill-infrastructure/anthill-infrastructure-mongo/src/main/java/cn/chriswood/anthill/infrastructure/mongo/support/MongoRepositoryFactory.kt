package cn.chriswood.anthill.infrastructure.mongo.support

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable


class MongoRepositoryFactory(
    private val template: MongoOperations,
    private val converter: MongoConverter
) : RepositoryFactorySupport() {


    // 实体信息缓存（Kotlin的委托属性实现）
    private val entityInfoCache = mutableMapOf<Class<*>, MongoEntityInformation<*, *>>()

    override fun <T, ID> getEntityInformation(domainClass: Class<T>): MongoEntityInformation<T, ID> {
        @Suppress("UNCHECKED_CAST")
        return entityInfoCache.getOrPut(domainClass) {
            MappingMongoEntityInformation(
                converter.mappingContext.getRequiredPersistentEntity(domainClass),
                Serializable::class.java
            )
        } as MongoEntityInformation<T, ID>
    }


    override fun getTargetRepository(information: RepositoryInformation): Any {
        return SimpleMongoRepository(
            getMongoEntityInformation(information),
            template
        )
    }

    private fun getMongoEntityInformation(
        information: RepositoryInformation
    ): MongoEntityInformation<Any, Serializable> {
        @Suppress("UNCHECKED_CAST")
        return getEntityInformation(information.domainType as Class<Any>)
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return SimpleMongoRepository::class.java
    }
}
