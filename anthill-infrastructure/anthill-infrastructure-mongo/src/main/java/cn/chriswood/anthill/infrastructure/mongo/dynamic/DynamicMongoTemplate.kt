package cn.chriswood.anthill.infrastructure.mongo.dynamic

import cn.chriswood.anthill.infrastructure.mongo.support.MongoConfigProperties
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MongoConverter

class DynamicMongoTemplate(
    private val mongoProperties: MongoConfigProperties,
    private val factory: DynamicMongoDatabaseFactory,
    private val dynamicMongoConverter: DynamicMongoConverter,
) {
    private var templates: MutableMap<String, MongoTemplate> = HashMap()
    private var initialized = false
    private fun init() {
        mongoProperties.dbs?.forEach { entry ->
            val name = entry.key
            val dbFactory: MongoDatabaseFactory = factory.getMongoDatabaseFactory(name)
            val converter: MongoConverter = dynamicMongoConverter.getConverter(name)
            val template = MongoTemplate(dbFactory, converter)
            templates[name] = template
        }
    }

    fun getTemplate(name: String): MongoTemplate {
        if (templates.isEmpty() && !initialized) {
            init()
        }
        return templates[name] ?: templates[factory.defaultDataSource]
        ?: throw IllegalStateException("No Template found")
    }

    fun getTemplate(): MongoTemplate {
        if (templates.isEmpty() && !initialized) {
            init()
        }
        return templates[DynamicMongoContextHolder.getDatabase()] ?: templates[factory.defaultDataSource]
        ?: throw IllegalStateException("No Template found")
    }

    fun addTemplate(name: String, template: MongoTemplate) {
        templates[name] = template
    }
}
