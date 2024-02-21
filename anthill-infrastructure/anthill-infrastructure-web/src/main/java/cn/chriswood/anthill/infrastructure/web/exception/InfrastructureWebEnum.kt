package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.ExceptionAssert
import cn.chriswood.anthill.infrastructure.web.utils.I18nMessageUtil

enum class InfrastructureWebEnum(
    private val code: Int,
    private val dialect: String,
) : ExceptionAssert {
    // 方法异常 格式class func error
    FUNC_ERROR(500, "FUNC_ERROR");

    fun getMessage(vararg args: Any?): String {
        return I18nMessageUtil.message(dialect, *args)
            ?: InfrastructureWebException.DEFAULT_MESSAGE
    }

    fun custom(message: String) {
        throw InfrastructureWebException(message, code, null)
    }

    override fun eject(vararg args: Any?) {
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

