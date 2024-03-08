package cn.chriswood.anthill.infrastructure.json.annotation

import cn.chriswood.anthill.infrastructure.json.support.TranslateSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@JsonSerialize(using = TranslateSerializer::class)
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
