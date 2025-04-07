package cn.chriswood.anthill.infrastructure.mongo.support

import DynamicMongoRepositoryFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport
import org.springframework.data.repository.core.support.RepositoryFactorySupport

class DynamicMongoRepositoryFactoryBean<T : Repository<S, ID>, S, ID>(
    repositoryInterface: Class<T>,
    private val templates: Map<String, MongoOperations>,
    private val converters: Map<String, MongoConverter>,
) : RepositoryFactoryBeanSupport<T, S, ID>(repositoryInterface) {

    override fun createRepositoryFactory(): RepositoryFactorySupport {
        return DynamicMongoRepositoryFactory(
            templates,
            converters,
        )
    }
}
