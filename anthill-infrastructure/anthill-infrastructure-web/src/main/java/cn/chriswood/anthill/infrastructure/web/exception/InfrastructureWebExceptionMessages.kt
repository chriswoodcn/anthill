package cn.chriswood.anthill.infrastructure.web.exception

object InfrastructureWebExceptionMessages {
    val messages = hashMapOf(
        "zh" to hashMapOf(
            "${InfrastructureWebException.DEFAULT_MODULE}.FUNC_ERROR" to "{0}类{1}方法调用异常,错误: {2}",
            "${InfrastructureWebException.DEFAULT_MODULE}.REPEAT_SUBMIT" to "{0}秒内不允许重复请求",
            "${InfrastructureWebException.DEFAULT_MODULE}.RATE_LIMIT" to "访问过于频繁，请稍候再试",
        ),
        "en" to hashMapOf(
            "${InfrastructureWebException.DEFAULT_MODULE}.FUNC_ERROR" to "class {0} function {} invoke exception，error: {2}",
            "${InfrastructureWebException.DEFAULT_MODULE}.REPEAT_SUBMIT" to "every {0} second permit repeatable request",
            "${InfrastructureWebException.DEFAULT_MODULE}.RATE_LIMIT" to "visit too frequently, please try again later",
        )
    )
}
