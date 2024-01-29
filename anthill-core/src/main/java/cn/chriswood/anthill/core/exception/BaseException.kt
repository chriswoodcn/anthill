package cn.chriswood.anthill.core.exception

import cn.chriswood.anthill.core.util.MessageUtil


open class BaseException(
    override var message: String,
    open var code: Int,
    open var module: String,
    vararg args: Any
) : RuntimeException() {
    companion object {
        const val DEFAULT_MESSAGE = "BaseException error"
        const val DEFAULT_MODULE = "Base"
    }

    constructor(code: Int, vararg args: Any) :
            this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, args) {
        val i18nMessage = MessageUtil.message(code, args)
        if (!i18nMessage.isNullOrEmpty()) {
            this.message = i18nMessage
        }
    }

    constructor(message: String, code: Int, vararg args: Any) :
            this(message, code, SystemException.DEFAULT_MODULE, args)
}
