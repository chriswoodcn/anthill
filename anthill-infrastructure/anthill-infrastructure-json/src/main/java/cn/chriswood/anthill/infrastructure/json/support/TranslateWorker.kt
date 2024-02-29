package cn.chriswood.anthill.infrastructure.json.support

interface TranslateWorker {
    fun <T> translation(key: Any?): T?
}
