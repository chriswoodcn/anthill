package cn.chriswood.anthill.infrastructure.webauth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.auth")
data class AuthProperties(val excludes: List<String> = listOf())
