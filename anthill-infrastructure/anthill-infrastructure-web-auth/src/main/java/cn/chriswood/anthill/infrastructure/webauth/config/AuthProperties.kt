package cn.chriswood.anthill.infrastructure.webauth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.web.auth")
data class AuthProperties(
    val excludes: List<String>? = listOf(),
    val enabled: Boolean?,
)
