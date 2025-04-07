import cn.chriswood.anthill.infrastructure.mongo.support.Constants
import cn.chriswood.anthill.infrastructure.mongo.support.DynamicMongoContextHolder
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.repository.query.MongoEntityInformation
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable

class DynamicMongoRepositoryFactory(
    private val templates: Map<String, MongoOperations>,
    private val converters: Map<String, MongoConverter>
) : RepositoryFactorySupport() {
    // 组合的组件
    private val defaultOperations = templates[Constants.PRIMARY]!!
    private val defaultConverter = converters[Constants.PRIMARY]!!

    // 实体信息缓存（Kotlin的委托属性实现）
    private val entityInfoCache = mutableMapOf<Class<*>, MongoEntityInformation<*, *>>()

    private fun getConverter(): MongoConverter {
        return converters[DynamicMongoContextHolder.getDatabase()] ?: defaultConverter
    }

    override fun <T, ID> getEntityInformation(domainClass: Class<T>): MongoEntityInformation<T, ID> {
        @Suppress("UNCHECKED_CAST")
        return entityInfoCache.getOrPut(domainClass) {
            MappingMongoEntityInformation(
                getConverter().mappingContext.getRequiredPersistentEntity(domainClass),
                Serializable::class.java
            )
        } as MongoEntityInformation<T, ID>
    }


    override fun getTargetRepository(information: RepositoryInformation): Any {
        return SimpleMongoRepository(
            getMongoEntityInformation(information),
            determineMongoOperations()
        )
    }

    private fun getMongoEntityInformation(
        information: RepositoryInformation
    ): MongoEntityInformation<Any, Serializable> {
        @Suppress("UNCHECKED_CAST")
        return getEntityInformation(information.domainType as Class<Any>)
    }

    private fun determineMongoOperations(): MongoOperations {
        return DynamicMongoContextHolder.getDatabase()?.let { ds ->
            templates[ds] ?: defaultOperations
        } ?: defaultOperations
    }


    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return SimpleMongoRepository::class.java
    }
}
