package cn.chriswood.anthill.infrastructure.web.base

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.exception.BaseException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

@NoArgs
@AllOpen
open class R<T>(
    open var code: Int,
    open var msg: String,
    open var data: T?
) {
    companion object {
        private const val SUCCESS = "SUCCESS"
        private const val FAIL = "FAIL"
        const val SUCCESS_CODE = 200
        const val FAIL_CODE = 500
        fun <T> ok(): R<T> {
            return R(SUCCESS_CODE, SUCCESS, null)
        }

        fun <T> ok(data: T?): R<T> {
            return R(SUCCESS_CODE, SUCCESS, data)
        }

        fun <T> ok(message: String?, data: T?): R<T> {
            return R(SUCCESS_CODE, message ?: SUCCESS, data)
        }

        fun <T> fail(): R<T> {
            return R(FAIL_CODE, FAIL, null)
        }

        fun <T> fail(message: String?): R<T> {
            return R(FAIL_CODE, message ?: FAIL, null)
        }

        fun <T> fail(code: Int, message: String?): R<T> {
            return R(code, message ?: FAIL, null)
        }

        fun fail(response: HttpServletResponse, exception: BaseException) {
            fail(response, exception.code, exception.message)
        }

        fun fail(response: HttpServletResponse, code: Int?, message: String?) {
            response.reset()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = StandardCharsets.UTF_8.name()
            val res = mutableMapOf<String, Any>()
            res["code"] = code ?: FAIL_CODE
            res["msg"] = message ?: FAIL
            response.writer.println(res)
        }
    }
}
