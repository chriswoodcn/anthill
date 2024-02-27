package cn.chriswood.anthill.mongo

import cn.chriswood.anthill.mongo.support.Constants
import cn.chriswood.anthill.mongo.support.MongoConfigProperties
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.convert.CustomConversions
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.convert.DbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@EnableMongoRepositories
@EnableTransactionManagement
//@ConditionalOnProperty(
//    prefix = "anthill.mongo",
//    name = ["enabled"],
//    havingValue = "true"
//)
@ConditionalOnExpression("!\${anthill.mongo.enabled}.equals('false')")
class MongoConfig {
    /**
     * mongoDB事务管理器(无副本集的情况下不可以使用事务)
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.mongo.transaction",
        name = ["enabled"],
        havingValue = "true"
    )
    fun transactionManager(
        factory: MongoDatabaseFactory
    ): MongoTransactionManager {
        return MongoTransactionManager(factory)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.mongo",
        name = ["type"],
        havingValue = Constants.SINGLE
    )
    fun mappingMongoConverter(
        factory: MongoDatabaseFactory,
        context: MongoMappingContext,
        beanFactory: BeanFactory
    ): MappingMongoConverter {
        val dbRefResolver: DbRefResolver = DefaultDbRefResolver(factory)
        val mappingConverter = MappingMongoConverter(dbRefResolver, context)
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions::class.java))
        } catch (ignore: NoSuchBeanDefinitionException) {
        }
        // 保存不需要 _class字段
        mappingConverter.setTypeMapper(DefaultMongoTypeMapper(null))
        return mappingConverter
    }
}
