package cn.chriswood.anthill.infrastructure.mongo.dynamic

object DynamicMongoContextHolder {
    private val contextHolder = ThreadLocal<String>()
    private val transactionHolder = ThreadLocal<String>()

    fun setTransaction(transaction: String) {
        transactionHolder.set(transaction)
    }

    fun clearTransaction() {
        transactionHolder.remove()
    }

    fun setDatabase(dbName: String) {
        contextHolder.set(dbName)
    }

    fun getDatabase(): String? {
        return transactionHolder.get() ?: contextHolder.get() ?: null
    }

    fun clearDatabase() {
        contextHolder.remove()
    }
}
