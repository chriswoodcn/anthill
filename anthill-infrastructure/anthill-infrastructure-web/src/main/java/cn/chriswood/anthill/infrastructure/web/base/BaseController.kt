package cn.chriswood.anthill.infrastructure.web.base

interface BaseController {
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    fun toAjax(rows: Int): R<Void> {
        return if (rows > 0) R.ok() else R.fail()
    }

    /**
     * 响应返回结果
     *
     * @param success 是否成功
     * @return 操作结果
     */
    fun toAjax(success: Boolean): R<Void> {
        return if (success) R.ok() else R.fail()
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    fun <T> toAjax(result: T): R<T> {
        return R.ok(result)
    }
}
