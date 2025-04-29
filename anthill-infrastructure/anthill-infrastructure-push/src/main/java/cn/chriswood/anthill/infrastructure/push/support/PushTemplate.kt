package cn.chriswood.anthill.infrastructure.push.support

interface PushTemplate {
    fun sendPush(message: Map<String, Any>)

    fun <T> getClient(): T?

    fun getKey(): String

    fun getSupplier(): String
}
