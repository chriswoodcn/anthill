package cn.chriswood.anthill.infrastructure.mongo

import DynamicMongoRepositoryFactory
import cn.chriswood.anthill.infrastructure.mongo.convert.*
import cn.chriswood.anthill.infrastructure.mongo.support.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.repository.core.support.RepositoryFactorySupport

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@ConditionalOnExpression(
    "#{'true'.equals(environment.getProperty('anthill.mongo.enabled'))}"
)
class MongoConfig(
    private val mongoConfigProperties: MongoConfigProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions.create {
            it.registerConverters(
                mutableListOf(
                    LocalDateTimeToBsonTimestampConvert(),
                    BsonTimestampToLocalDateTimeConvert(),
                    BigDecimalToDecimal128Convert(),
                    Decimal128ToBigDecimalConvert(),
                    LongToBsonTimestampConvert(),
                    BsonTimestampToLongConvert()
                )
            )
        }
    }

    @Bean
    fun mongoMappingContext(): MongoMappingContext {
        val context = MongoMappingContext()
        context.isAutoIndexCreation = true
        context.setFieldNamingStrategy(SnakeCaseFieldNamingStrategy())
        return context
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.mongo", name = ["type"], havingValue = Constants.SINGLE
    )
    fun mongoDatabaseFactory(): MongoDatabaseFactory {
        val uri = mongoConfigProperties.dbs!![Constants.PRIMARY]!!.uri!!
        return SimpleMongoClientDatabaseFactory(uri)
    }

    @Bean
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun mongoTemplate(
        factory: MongoDatabaseFactory, converter: MongoConverter
    ): MongoTemplate {
        return MongoTemplate(factory, converter)
    }

    /**
     * 自定义mappingMongoConverter去除_class字段
     */
    @Bean
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun dataSourceConverter(
        factory: MongoDatabaseFactory, conversions: MongoCustomConversions, context: MongoMappingContext
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
    @ConditionalOnBean(MongoDatabaseFactory::class)
    fun mongoRepositoryFactory(
        template: MongoTemplate,
        converter: MongoConverter
    ): RepositoryFactorySupport {
        return MongoRepositoryFactory(template, converter)
    }

    @Bean("dataSourceConverters")
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dataSourceConverters(
        factory: DynamicMongoDatabaseFactory,
        conversions: MongoCustomConversions,
        context: MongoMappingContext
    ): MutableMap<String, MongoConverter> {
        val converters: MutableMap<String, MongoConverter> = HashMap()
        factory.getDataSourceNames().forEach { name ->
            val dbRefResolver: DbRefResolver = DefaultDbRefResolver(
                factory.getMongoDatabaseFactory(name)
            )
            val converter = MappingMongoConverter(dbRefResolver, context)
            converter.setTypeMapper(DefaultMongoTypeMapper(null))
            converter.setCustomConversions(conversions)
            converter.afterPropertiesSet()
            converters[name] = converter
        }
        return converters
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

    @Bean("dataSourceTemplates")
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun mongoTemplates(
        factory: DynamicMongoDatabaseFactory, datasourceConverters: Map<String, MappingMongoConverter>
    ): MutableMap<String, MongoTemplate> {
        val templates: MutableMap<String, MongoTemplate> = HashMap()
        mongoConfigProperties.dbs?.forEach { entry ->
            val name = entry.key
            val dbFactory: MongoDatabaseFactory = factory.getMongoDatabaseFactory(name)
            val converter: MappingMongoConverter? = datasourceConverters[name]
            val template = MongoTemplate(dbFactory, converter)
            templates[name] = template
        }
        return templates
    }

    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoDataSourceManager(factory: DynamicMongoDatabaseFactory): DynamicMongoDataSourceManager {
        return DynamicMongoDataSourceManager(factory)
    }

    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun dynamicMongoRepositoryFactory(
        templates: MutableMap<String, MongoTemplate>,
        converters: MutableMap<String, MongoConverter>
    ): RepositoryFactorySupport {
        return DynamicMongoRepositoryFactory(
            templates, converters
        )
    }

    @Bean
    @ConditionalOnBean(DynamicMongoDatabaseFactory::class)
    fun validateDynamicMongoDatabaseConfig(properties: MongoConfigProperties): CommandLineRunner {
        return CommandLineRunner {
            check(properties.dbs.isNullOrEmpty()) { "No MongoDB datasource configured" }
            val filterKeys = properties.dbs!!.filterKeys { it == Constants.PRIMARY }
            check(filterKeys.isEmpty()) { "Primary MongoDB datasource not configured" }
            properties.dbs.forEach { entry ->
                checkNotNull(entry.value.uri) { "MongoDB URI not configured for datasource: ${entry.key}" }
                log.debug("MongoDB Datasource: {} configured", entry.key)
            }
        }
    }
}
