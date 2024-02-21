package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.BaseException
import cn.chriswood.anthill.infrastructure.web.utils.I18nMessageUtil

class InfrastructureWebException(
    override var message: String,
    override var code: Int,
    override var module: String,
    override var dialect: String?,
    vararg args: Any?
) : BaseException(message, code, module) {
    companion object {
        const val DEFAULT_MESSAGE = "InfrastructureWebException error"
        const val DEFAULT_MODULE = "InfrastructureWeb"
    }

    constructor(code: Int, dialect: String?, vararg args: Any?) :
        this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, dialect, *args) {
        val i18nMessage = I18nMessageUtil.message(
            "$DEFAULT_MODULE.$dialect", *args
        )
        if (!i18nMessage.isNullOrEmpty()) {
            this.message = i18nMessage
        }
    }

    constructor(message: String, code: Int, dialect: String?, vararg args: Any?) :
        this(message, code, DEFAULT_MODULE, dialect, *args)
}
