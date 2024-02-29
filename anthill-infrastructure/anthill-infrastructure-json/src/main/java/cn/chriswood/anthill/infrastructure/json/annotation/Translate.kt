package cn.chriswood.anthill.infrastructure.json.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Translate(
    val key: String = "",
    val mapper: String = "",
)
