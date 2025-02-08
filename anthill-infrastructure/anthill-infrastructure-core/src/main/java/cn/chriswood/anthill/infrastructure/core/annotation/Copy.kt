package cn.chriswood.anthill.infrastructure.core.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Copy(
    val value: KClass<out CopyConverter<*>>,
    val target: String = ""
)
