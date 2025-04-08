package cn.chriswood.anthill.infrastructure.mongo.dynamic

import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

class DynamicMongoConverter(
    private val factory: DynamicMongoDatabaseFactory,
    private val conversions: MongoCustomConversions,
    private val context: MongoMappingContext
) {
    private val converters: MutableMap<String, MongoConverter> = HashMap()
    private var initialized = false

    private fun init() {
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
        initialized = true
    }

    fun getConverter(name: String): MongoConverter {
        if (converters.isEmpty() && !initialized) {
            init()
        }
        return converters[name] ?: converters[factory.defaultDataSource]
        ?: throw IllegalStateException("No converter found")
    }

    fun addConverter(name: String, converter: MappingMongoConverter) {
        converters[name] = converter
    }
}
