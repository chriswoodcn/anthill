package cn.chriswood.anthill.infrastructure.web.exception

object InfrastructureWebExceptionMessages {
    val messages = hashMapOf(
        "zh" to hashMapOf(
            "${InfrastructureWebException.DEFAULT_MODULE}.REPEAT_SUBMIT" to "{0}秒内不允许重复请求",
        ),
        "en" to hashMapOf(
            "${InfrastructureWebException.DEFAULT_MODULE}.REPEAT_SUBMIT" to "every {0} second permit repeatable request",
        )
    )
}
