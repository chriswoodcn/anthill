package cn.chriswood.anthill.infrastructure.mongo

import cn.chriswood.anthill.infrastructure.mongo.dynamic.DynamicMongoDatabaseFactory
import cn.chriswood.anthill.infrastructure.mongo.dynamic.DynamicMongoTransactionManager
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.mongo.transaction", name = ["enabled"], havingValue = "true"
)
class MongoTransactionConfig {

    /**
     * 单体mongoDB事务管理器(无副本集的情况下不可以使用事务)
     */
    @Bean
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun mongoTransactionManager(
        factory: MongoDatabaseFactory
    ): MongoTransactionManager {
        return MongoTransactionManager(factory)
    }


    /**
     * 动态数据源mongoDB事务管理器(无副本集的情况下不可以使用事务)
     */
    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun mongoDynamicTransactionManager(): MongoTransactionManager {
        return DynamicMongoTransactionManager()
    }
}
