package cn.chriswood.anthill.infrastructure.mongo

import cn.chriswood.anthill.infrastructure.mongo.support.Constants
import cn.chriswood.anthill.infrastructure.mongo.support.MongoConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@ConditionalOnExpression(
    "#{'true'.equals(environment.getProperty('anthill.mongo.enabled')) " +
        "&& 'single'.equals(environment.getProperty('anthill.mongo.enabled'))}"
)
class SingleMongoConfig(
    private val mongoConfigProperties: MongoConfigProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.mongo", name = ["type"], havingValue = Constants.SINGLE
    )
    fun singleMongoDatabaseFactory(): MongoDatabaseFactory {
        val uri = mongoConfigProperties.dbs!![Constants.PRIMARY]!!.uri!!
        return SimpleMongoClientDatabaseFactory(uri)
    }

    @Bean("singleMongoTemplate")
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun singleMongoTemplate(
        factory: MongoDatabaseFactory, converter: MongoConverter
    ): MongoTemplate {
        return MongoTemplate(factory, converter)
    }

    /**
     * 自定义mappingMongoConverter去除_class字段
     */
    @Bean("singleMongoConverter")
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun singleMongoConverter(
        factory: MongoDatabaseFactory,
        conversions: MongoCustomConversions,
        context: MongoMappingContext
    ): MongoConverter {
        val dbRefResolver: DbRefResolver = DefaultDbRefResolver(factory)
        val mappingConverter = MappingMongoConverter(dbRefResolver, context)
        // 添加自定义converter
        mappingConverter.setCustomConversions(conversions)
        // 保存不需要 _class字段
        mappingConverter.setTypeMapper(DefaultMongoTypeMapper(null))
        return mappingConverter
    }

    @Bean
    fun validateSingleMongoDatabaseConfig(properties: MongoConfigProperties): CommandLineRunner {
        return CommandLineRunner {
            check(!properties.dbs.isNullOrEmpty()) { "No MongoDB datasource configured" }
            val filterKeys = properties.dbs.filterKeys { it == Constants.PRIMARY }
            check(filterKeys.isNotEmpty()) { "Primary MongoDB datasource not configured" }
        }
    }
}
