package cn.chriswood.anthill.infrastructure.mongo.support

import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

class DynamicMongoDatabaseFactory(
    val factories: MutableMap<String, MongoDatabaseFactory>
) : AbstractMongoClientConfiguration() {

    val defaultDataSource = Constants.PRIMARY
    val transactionDataSource = ThreadLocal<String>()

    fun setTransactionDataSource(dataSourceName: String?) {
        transactionDataSource.set(dataSourceName)
    }

    fun clearTransactionDataSource() {
        transactionDataSource.remove()
    }

    override fun mongoDbFactory(): MongoDatabaseFactory {
        // 事务期间优先使用事务绑定的数据源
        var dataSourceName = transactionDataSource.get()

        if (dataSourceName == null) {
            dataSourceName = DynamicMongoContextHolder.getDatabase() ?: defaultDataSource
        }

        return factories[dataSourceName]
            ?: throw IllegalStateException("No MongoDatabaseFactory configured for: $dataSourceName")
    }

    override fun getDatabaseName(): String {
        return mongoDbFactory().mongoDatabase.name;
    }

    fun getMongoDatabaseFactory(name: String): MongoDatabaseFactory {
        return factories[name]
            ?: throw IllegalStateException("No MongoDatabaseFactory configured for: $name")
    }

    fun getDataSourceNames(): List<String> {
        return factories.keys.toList()
    }

}
