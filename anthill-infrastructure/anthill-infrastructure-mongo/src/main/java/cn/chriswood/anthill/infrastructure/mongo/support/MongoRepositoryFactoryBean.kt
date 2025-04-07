package cn.chriswood.anthill.infrastructure.mongo.support

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport
import org.springframework.data.repository.core.support.RepositoryFactorySupport

class MongoRepositoryFactoryBean<T : Repository<S, ID>, S, ID>(
    repositoryInterface: Class<T>,
    private val template: MongoOperations,
    private val converter:MongoConverter,
) : RepositoryFactoryBeanSupport<T, S, ID>(repositoryInterface) {

    override fun createRepositoryFactory(): RepositoryFactorySupport {
        return MongoRepositoryFactory(
            template,
            converter,
        )
    }
}
