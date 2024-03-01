package cn.chriswood.anthill.infrastructure.json.annotation

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Translate(
    /**
     * 翻译的key
     */
    val key: String = "",
    /**
     * 映射的属性
     */
    val mapper: String = "",

    /**
     * custom
     */
    val custom: String = "",
)
