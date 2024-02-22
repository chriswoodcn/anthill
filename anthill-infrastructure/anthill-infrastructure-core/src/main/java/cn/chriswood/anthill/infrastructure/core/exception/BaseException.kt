package cn.chriswood.anthill.infrastructure.core.exception

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil

open class BaseException(
    override var message: String,
    open var code: Int,
    open var module: String,
    open var dialect: String?,
) : RuntimeException() {
    companion object {
        const val DEFAULT_MESSAGE = "BaseException error"
        const val DEFAULT_MODULE = "Base"
    }

    constructor(code: Int, dialect: String?, vararg args: Any?) :
        this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, dialect) {
        val i18nMessage = I18nMessageUtil.message(
            "$DEFAULT_MODULE.$dialect", *args
        )
        if (!i18nMessage.isNullOrEmpty()) {
            this.message = i18nMessage
        }
    }

    constructor(message: String, code: Int, dialect: String?, vararg args: Any?) :
        this(message, code, DEFAULT_MODULE, dialect) {
        val i18nMessage = I18nMessageUtil.message(
            "${DEFAULT_MODULE}.$dialect", *args
        )
        if (!i18nMessage.isNullOrEmpty()) {
            this.message = i18nMessage
        }
    }
}
