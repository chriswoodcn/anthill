package cn.chriswood.anthill.infrastructure.core.exception

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil

open class BaseException(
    open var code: Int,
    open var module: String = DEFAULT_MODULE,
    open var dialect: String,
    open var args: Array<out Any?> = emptyArray()
) : RuntimeException() {
    private var customMessage: Boolean = false
    override var message: String = DEFAULT_MESSAGE
        set(value) {
            customMessage = true
            field = value
        }
        get() {
            if (customMessage) return field
            val i18nMessage = I18nMessageUtil.message(
                "${module}.$dialect", *args
            )
            return if (!i18nMessage.isNullOrEmpty()) {
                i18nMessage
            } else {
                DEFAULT_MESSAGE
            }
        }

    companion object {
        const val DEFAULT_MESSAGE = "BaseException error"
        const val DEFAULT_MODULE = "Base"
    }
}
