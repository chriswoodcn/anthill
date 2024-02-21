package cn.chriswood.anthill.infrastructure.core.exception

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil

enum class InfrastructureExceptionEnum(
    private val code: Int,
    private val dialect: String,
) : ExceptionAssert {
    // 方法调用异常 格式class func error
    FUNC_ERROR(500, "FUNC_ERROR");

    fun getMessage(vararg args: Any?): String {
        return I18nMessageUtil.message(dialect, *args)
            ?: InfrastructureException.DEFAULT_MESSAGE
    }

    fun custom(message: String) {
        throw InfrastructureException(message, code, null)
    }

    override fun eject(vararg args: Any?) {
        throw InfrastructureException(code, dialect, *args)
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?) {
        if (!expression) {
            throw InfrastructureException(code, dialect, *args)
        }
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?, callback: () -> Unit?) {
        if (!expression) {
            callback()
            throw InfrastructureException(code, dialect, *args)
        }
    }
}

