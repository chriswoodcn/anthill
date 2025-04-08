package cn.chriswood.anthill.infrastructure.mongo.dynamic

import cn.hutool.extra.spring.SpringUtil
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoMappingContext


class DynamicMongoDataSourceManager(
    private val databaseFactory: DynamicMongoDatabaseFactory
) {
    fun addDataSource(name: String, uri: String) {
        val factory: MongoDatabaseFactory = SimpleMongoClientDatabaseFactory(uri)

        try {
            //DynamicMongoDatabaseFactory需要修改
            val dynamicMongoDatabaseFactory = SpringUtil.getBean(DynamicMongoDatabaseFactory::class.java)
            dynamicMongoDatabaseFactory.addFactory(name, factory)
            //DynamicMongoConverter
            val dynamicMongoConverter = SpringUtil.getBean(DynamicMongoConverter::class.java)
            val dbRefResolver: DbRefResolver = DefaultDbRefResolver(factory)
            val converter = MappingMongoConverter(dbRefResolver, SpringUtil.getBean(MongoMappingContext::class.java))
            converter.setTypeMapper(DefaultMongoTypeMapper(null))
            converter.setCustomConversions(SpringUtil.getBean(MongoCustomConversions::class.java))
            dynamicMongoConverter.addConverter(name, converter)
            //DynamicMongoTemplate
            val dynamicMongoTemplate = SpringUtil.getBean(DynamicMongoTemplate::class.java)
            val template = MongoTemplate(factory, converter)
            dynamicMongoTemplate.addTemplate(name, template)
        } catch (e: Exception) {
            throw RuntimeException("Failed to add mongo data source", e)
        }
    }
}
