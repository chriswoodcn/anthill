package cn.chriswood.anthill.infrastructure.mongo.dynamic

import cn.chriswood.anthill.infrastructure.mongo.support.Constants
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

class DynamicMongoDatabaseFactory(
    private val factories: MutableMap<String, MongoDatabaseFactory>
) : AbstractMongoClientConfiguration() {
    private final val storeFactories = mutableMapOf<String, MongoDatabaseFactory>()
    val defaultDataSource = Constants.PRIMARY

    init {
        storeFactories.putAll(factories)
    }


    override fun mongoDbFactory(): MongoDatabaseFactory {
        // 事务期间优先使用事务绑定的数据源

        val dataSourceName = DynamicMongoContextHolder.getDatabase() ?: defaultDataSource


        return storeFactories[dataSourceName]
            ?: throw IllegalStateException("No MongoDatabaseFactory configured for: $dataSourceName")
    }

    override fun getDatabaseName(): String {
        return mongoDbFactory().mongoDatabase.name;
    }

    fun getMongoDatabaseFactory(name: String): MongoDatabaseFactory {
        return storeFactories[name]
            ?: throw IllegalStateException("No MongoDatabaseFactory configured for: $name")
    }

    fun getDataSourceNames(): List<String> {
        return storeFactories.keys.toList()
    }

    fun addFactory(name: String, factory: MongoDatabaseFactory) {
        storeFactories[name] = factory
    }
}
