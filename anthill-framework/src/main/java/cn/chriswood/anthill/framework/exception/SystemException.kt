package cn.chriswood.anthill.framework.exception

import cn.chriswood.anthill.framework.utils.I18nMessageUtil

class SystemException(
    override var message: String,
    override var code: Int,
    override var module: String,
    vararg args: Any
) : BaseException(message, code, module, args) {
    companion object {
        const val DEFAULT_MESSAGE = "SystemException error"
        const val DEFAULT_MODULE = "System"
    }

    constructor(code: Int, vararg args: Any) :
            this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, args) {
        val i18nMessage = I18nMessageUtil.message(code, args)
        if (!i18nMessage.isNullOrEmpty()) {
            this.message = i18nMessage
        }
    }

    constructor(message: String, code: Int, vararg args: Any) :
            this(message, code, DEFAULT_MODULE, args)
}
