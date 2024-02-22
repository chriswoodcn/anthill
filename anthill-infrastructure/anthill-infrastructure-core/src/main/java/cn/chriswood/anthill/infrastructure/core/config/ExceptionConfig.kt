package cn.chriswood.anthill.infrastructure.core.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.exception")
data class ExceptionConfig(
    val enableCustomMessage: Boolean = false,
    val customMessageModule: List<String> = emptyList(),
)
