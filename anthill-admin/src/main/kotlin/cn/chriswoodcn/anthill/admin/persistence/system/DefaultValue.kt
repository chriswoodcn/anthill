package com.taotao.bmm.persistence.system

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DefaultValue(
    val value: String = ""
)
