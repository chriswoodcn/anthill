package cn.chriswood.anthill.infrastructure.datasource.common

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.jpa")
data class JpaTypeProperty(var type: String)
