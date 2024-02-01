package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.ExceptionAssert
import cn.chriswood.anthill.infrastructure.web.utils.I18nMessageUtil

enum class SystemExceptionEnum(
    private val code: Int
) : ExceptionAssert {
    SEVER_ERROR(500),

    // 格式filename uname error
    SEVER_FUNC_ERROR(50000);

    fun getMessage(vararg args: Any?): String {
        return I18nMessageUtil.message(code, args) ?: SystemException.DEFAULT_MESSAGE
    }

    fun custom(message: String) {
        throw SystemException(message, code)
    }

    override fun eject(vararg args: Any?) {
        throw SystemException(code, args)
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?) {
        if (!expression) {
            throw SystemException(code, args)
        }
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?, callback: () -> Unit?) {
        if (!expression) {
            callback()
            throw SystemException(code, args)
        }
    }
}
