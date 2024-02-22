package cn.chriswood.anthill.infrastructure.core.exception

object InfrastructureExceptionMessages {
    val messages = hashMapOf(
        "zh" to hashMapOf(
            "${InfrastructureException.DEFAULT_MODULE}.FUNC_ERROR" to "{0}类{1}方法调用失败,原因:{2}"
        ),
        "en" to hashMapOf(
            "${InfrastructureException.DEFAULT_MODULE}.FUNC_ERROR" to "{0} Class {1} function invoke fail, reason: {2}"
        )
    )
}
