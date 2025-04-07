package cn.chriswood.anthill.infrastructure.mongo.support

object DynamicMongoContextHolder {
    private val contextHolder = ThreadLocal<String>()
    fun setDatabase(dbName: String) {
        contextHolder.set(dbName)
    }

    fun getDatabase(): String? {
        return contextHolder.get() ?: null
    }

    fun clear() {
        contextHolder.remove()
    }
}
