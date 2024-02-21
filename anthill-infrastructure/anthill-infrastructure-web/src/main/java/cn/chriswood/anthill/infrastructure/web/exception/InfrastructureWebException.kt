package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.InfrastructureException

class InfrastructureWebException(
    override var message: String,
    override var code: Int,
    override var module: String,
    override var dialect: String?,
    vararg args: Any?
) : InfrastructureException(message, code, module, dialect) {
    companion object {
        const val DEFAULT_MESSAGE = "InfrastructureWebException error"
        const val DEFAULT_MODULE = "InfrastructureWeb"
    }

    constructor(code: Int, dialect: String?, vararg args: Any?) :
        this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, dialect, *args)

    constructor(message: String, code: Int, dialect: String?, vararg args: Any?) :
        this(message, code, DEFAULT_MODULE, dialect, *args)
}

