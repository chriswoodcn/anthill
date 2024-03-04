package cn.chriswood.anthill.infrastructure.web.base

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
    }
}
