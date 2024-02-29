package cn.chriswood.anthill.infrastructure.json.annotation

import cn.chriswood.anthill.infrastructure.json.support.SensitiveSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * 数据脱敏注解
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@JsonSerialize(using = SensitiveSerializer::class)
annotation class Sensitive(
    val strategy: SensitiveStrategy = SensitiveStrategy.PASSWORD,
    val role: String = "",
    val perm: String = "",
)
