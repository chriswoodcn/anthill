package cn.chriswood.anthill.infrastructure.core.exception

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
        this(DEFAULT_MESSAGE, code, DEFAULT_MODULE, args)

    constructor(message: String, code: Int, vararg args: Any) :
        this(message, code, DEFAULT_MODULE, args)
}
