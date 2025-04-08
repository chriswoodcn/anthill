package cn.chriswood.anthill.infrastructure.mongo

import cn.chriswood.anthill.infrastructure.mongo.dynamic.*
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
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@ConditionalOnExpression(
    "#{'true'.equals(environment.getProperty('anthill.mongo.enabled')) " +
        "&& 'dynamic'.equals(environment.getProperty('anthill.mongo.enabled'))}"
)
class DynamicMongoConfig(
    private val mongoConfigProperties: MongoConfigProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean("dynamicMongoConverter")
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoConverter(
        factory: DynamicMongoDatabaseFactory,
        conversions: MongoCustomConversions,
        context: MongoMappingContext
    ): DynamicMongoConverter {
        return DynamicMongoConverter(factory, conversions, context)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.mongo", name = ["type"], havingValue = Constants.DYNAMIC
    )
    fun dynamicMongoDatabaseFactory(): DynamicMongoDatabaseFactory {
        val factories: MutableMap<String, MongoDatabaseFactory> = HashMap()
        mongoConfigProperties.dbs?.forEach { (name, properties) ->
            if (properties.uri != null) {
                val factory: MongoDatabaseFactory = SimpleMongoClientDatabaseFactory(properties.uri)
                factories[name] = factory
            }
        }
        return DynamicMongoDatabaseFactory(factories)
    }

    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoDatabaseAspect(): DynamicMongoDataSourceAspect {
        return DynamicMongoDataSourceAspect()
    }

    @Bean("dynamicMongoTemplate")
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoTemplate(
        factory: DynamicMongoDatabaseFactory,
        dynamicMongoConverter: DynamicMongoConverter
    ): DynamicMongoTemplate {
        return DynamicMongoTemplate(mongoConfigProperties, factory, dynamicMongoConverter)
    }

    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoDataSourceManager(factory: DynamicMongoDatabaseFactory): DynamicMongoDataSourceManager {
        return DynamicMongoDataSourceManager(factory)
    }

    @Bean
    fun validateDynamicMongoDatabaseConfig(properties: MongoConfigProperties): CommandLineRunner {
        return CommandLineRunner {
            check(!properties.dbs.isNullOrEmpty()) { "No MongoDB datasource configured" }
            val filterKeys = properties.dbs.filterKeys { it == Constants.PRIMARY }
            check(filterKeys.isNotEmpty()) { "Primary MongoDB datasource not configured" }
            properties.dbs.forEach { entry ->
                checkNotNull(entry.value.uri) { "MongoDB URI not configured for datasource: ${entry.key}" }
                log.debug("MongoDB Datasource: {} configured", entry.key)
            }
        }
    }
}
