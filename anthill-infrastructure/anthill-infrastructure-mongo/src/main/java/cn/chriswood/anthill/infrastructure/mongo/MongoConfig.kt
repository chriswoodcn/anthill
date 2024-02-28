package cn.chriswood.anthill.infrastructure.mongo

import cn.chriswood.anthill.infrastructure.mongo.convert.BigDecimalToDecimal128Convert
import cn.chriswood.anthill.infrastructure.mongo.convert.BsonTimestampToLocalDateTimeConvert
import cn.chriswood.anthill.infrastructure.mongo.convert.Decimal128ToBigDecimalConvert
import cn.chriswood.anthill.infrastructure.mongo.convert.LocalDateTimeToBsonTimestampConvert
import cn.chriswood.anthill.infrastructure.mongo.support.Constants
import cn.chriswood.anthill.infrastructure.mongo.support.MongoConfigProperties
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@EnableMongoRepositories
@EnableTransactionManagement
@ConditionalOnExpression(
    "#{'true'.equals(environment.getProperty('anthill.mongo.enabled'))}"
)
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

    /**
     * 如果是单个mongodb实例 自定义mappingMongoConverter去除_class字段
     */
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
            mappingConverter.setCustomConversions(beanFactory.getBean(MongoCustomConversions::class.java))
        } catch (ignore: NoSuchBeanDefinitionException) {
        }
        // 保存不需要 _class字段
        mappingConverter.setTypeMapper(DefaultMongoTypeMapper(null))
        return mappingConverter
    }

    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions.create {
            it.registerConverters(
                mutableListOf(
                    LocalDateTimeToBsonTimestampConvert(),
                    BsonTimestampToLocalDateTimeConvert(),
                    BigDecimalToDecimal128Convert(),
                    Decimal128ToBigDecimalConvert()
                )
            )
        }
    }

}
