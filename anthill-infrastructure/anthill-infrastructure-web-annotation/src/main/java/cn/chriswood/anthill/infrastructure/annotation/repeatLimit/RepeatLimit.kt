package cn.chriswood.anthill.infrastructure.annotation.repeatLimit

import java.util.concurrent.TimeUnit


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RepeatLimit(
    val interval: Long = 1000L,
    val timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    val dialect: String = "RepeatLimit.message",
    val message: String = "forbid repeat submit",
)
