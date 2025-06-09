package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.ExceptionAssert

enum class InfrastructureWebExceptionEnum(
    val code: Int,
    var dialect: String,
) : ExceptionAssert {
    // 方法调用异常 格式class func error
    FUNC_ERROR(500, "FUNC_ERROR"),
    REPEAT_SUBMIT(49000, "REPEAT_SUBMIT"),
    RATE_LIMIT(49001, "RATE_LIMIT");

    override fun eject(vararg args: Any?): Nothing {
        throw InfrastructureWebException(code, dialect, *args)
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?) {
        if (!expression) {
            throw InfrastructureWebException(code, dialect, *args)
        }
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?, callback: () -> Unit?) {
        if (!expression) {
            callback()
            throw InfrastructureWebException(code, dialect, *args)
        }
    }
}

