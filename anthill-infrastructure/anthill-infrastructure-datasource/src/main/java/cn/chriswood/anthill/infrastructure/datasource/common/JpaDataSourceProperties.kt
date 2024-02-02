package cn.chriswood.anthill.infrastructure.datasource.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties("anthill.jpa")
data class JpaDataSourceProperties(
    val multi: Map<String, JpaDataSourceProperty>
)
