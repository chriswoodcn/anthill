package cn.chriswood.anthill.framework.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(ApplicationConfig::class)
@ConfigurationProperties("application")
data class ApplicationConfig(
    val name: String,
    val version: String
)
