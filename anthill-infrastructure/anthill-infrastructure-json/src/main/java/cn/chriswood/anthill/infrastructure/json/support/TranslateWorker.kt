package cn.chriswood.anthill.infrastructure.json.support

interface TranslateWorker {
    fun <T> translation(mapper: Any?, custom: String): T?
}
