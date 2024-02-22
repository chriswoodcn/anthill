package cn.chriswood.anthill.infrastructure.web.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.web.auth")
data class AuthProperties(
    val excludes: List<String> = emptyList(),
    val enabled: Boolean?,
)
