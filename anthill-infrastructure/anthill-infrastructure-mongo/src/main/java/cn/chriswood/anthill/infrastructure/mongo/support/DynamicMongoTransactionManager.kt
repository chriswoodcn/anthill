package cn.chriswood.anthill.infrastructure.mongo.support

import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.transaction.TransactionDefinition

class DynamicMongoTransactionManager(
    private val databaseFactory: DynamicMongoDatabaseFactory
) : MongoTransactionManager() {

    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        // 在事务开始前检查数据源
        val dataSourceName = DynamicMongoContextHolder.getDatabase()
            ?: throw IllegalStateException("No data source specified for transaction")

        // 设置事务线程使用固定数据源
        databaseFactory.setTransactionDataSource(dataSourceName)

        try {
            super.doBegin(transaction, definition)
        } catch (e: Exception) {
            databaseFactory.clearTransactionDataSource()
            throw e
        }
    }

    override fun doCleanupAfterCompletion(transaction: Any) {
        try {
            super.doCleanupAfterCompletion(transaction)
        } finally {
            databaseFactory.clearTransactionDataSource()
        }
    }
}
