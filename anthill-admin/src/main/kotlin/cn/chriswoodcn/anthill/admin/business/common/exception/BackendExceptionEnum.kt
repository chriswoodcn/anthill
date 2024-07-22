package com.taotao.bmm.business.common.exception

import cn.chriswood.anthill.infrastructure.core.exception.ExceptionAssert

enum class BackendExceptionEnum(
    private val code: Int,
    private val dialect: String,
) : ExceptionAssert {
    USER_NOT_EXIST(40001, "USER_NOT_EXIST"),
    USER_PASSWORD_ERR(40002, "USER_PASSWORD_ERR"),
    NOT_ALLOWED_EDIT(40003, "NOT_ALLOWED_EDIT"),
    USER_EMAIL_EXIST(40004, "USER_EMAIL_EXIST"),
    USER_FROZEN(40005, "USER_FROZEN"),
    COMPANY_ID_EXIST(40006, "COMPANY_ID_EXIST"),
    SERVER_ERROR(500, "SERVER_ERROR");

    override fun eject(vararg args: Any?) {
        throw BackendException(code, dialect, *args)
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?) {
        if (!expression) {
            throw BackendException(code, dialect, *args)
        }
    }

    override fun stateTrue(expression: Boolean, vararg args: Any?, callback: () -> Unit?) {
        if (!expression) {
            callback()
            throw BackendException(code, dialect, *args)
        }
    }
}