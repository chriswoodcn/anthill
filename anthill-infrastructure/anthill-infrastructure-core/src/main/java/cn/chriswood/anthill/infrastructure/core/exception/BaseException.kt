package cn.chriswood.anthill.infrastructure.core.exception

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

    constructor(code: Int, dialect: String?) :
        this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, dialect)

    constructor(message: String, code: Int, dialect: String?) :
        this(message, code, DEFAULT_MODULE, dialect)
}
