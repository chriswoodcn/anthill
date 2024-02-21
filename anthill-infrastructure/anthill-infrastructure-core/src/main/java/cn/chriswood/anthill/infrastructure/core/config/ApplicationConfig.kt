package cn.chriswood.anthill.infrastructure.core.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.application")
data class ApplicationConfig(
    val name: String,
    val version: String,
    val description: String?,
    val author: String?,
    val email: String?,
)
