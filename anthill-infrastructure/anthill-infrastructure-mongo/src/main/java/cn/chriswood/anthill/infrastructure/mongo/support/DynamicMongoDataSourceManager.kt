package cn.chriswood.anthill.infrastructure.mongo.support

import cn.hutool.core.util.ReflectUtil
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

        // 使用反射或其他方式访问DynamicMongoDatabaseFactory中的factories map
        try {
            val factoriesField = DynamicMongoDatabaseFactory::class.java.getDeclaredField("factories")
            factoriesField.setAccessible(true)
            val fieldValue = ReflectUtil.getFieldValue(databaseFactory, factoriesField)
            if (fieldValue is MutableMap<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val factoriesMap = fieldValue as MutableMap<String, MongoDatabaseFactory>
                factoriesMap[name] = factory
            }
            ReflectUtil.setFieldValue(databaseFactory, factoriesField, fieldValue)

            val datasourceTemplates = SpringUtil.getBean<MutableMap<String, MongoTemplate>>("datasourceTemplates")
            datasourceTemplates[name] = MongoTemplate(factory)

            val datasourceConverters = SpringUtil.getBean<MutableMap<String, MongoConverter>>("datasourceConverters")
            val dbRefResolver: DbRefResolver = DefaultDbRefResolver(factory)
            val converter = MappingMongoConverter(dbRefResolver, SpringUtil.getBean(MongoMappingContext::class.java))
            converter.setTypeMapper(DefaultMongoTypeMapper(null))
            converter.setCustomConversions(SpringUtil.getBean(MongoCustomConversions::class.java))
            converter.afterPropertiesSet()
            datasourceConverters[name] = converter

        } catch (e: Exception) {
            throw RuntimeException("Failed to add data source", e)
        }
    }
}
