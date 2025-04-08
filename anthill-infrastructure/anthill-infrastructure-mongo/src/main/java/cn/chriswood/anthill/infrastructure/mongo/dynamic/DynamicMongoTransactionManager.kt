package cn.chriswood.anthill.infrastructure.mongo.dynamic

import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.transaction.TransactionDefinition

class DynamicMongoTransactionManager : MongoTransactionManager() {

    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        // 在事务开始前检查数据源
        val dataSourceName = DynamicMongoContextHolder.getDatabase()
            ?: throw IllegalStateException("No data source specified for transaction")

        // 设置事务线程使用固定数据源
        DynamicMongoContextHolder.setTransaction(dataSourceName)

        try {
            super.doBegin(transaction, definition)
        } catch (e: Exception) {
            DynamicMongoContextHolder.clearTransaction()
            throw e
        }
    }

    override fun doCleanupAfterCompletion(transaction: Any) {
        try {
            super.doCleanupAfterCompletion(transaction)
        } finally {
            DynamicMongoContextHolder.clearTransaction()
        }
    }
}
