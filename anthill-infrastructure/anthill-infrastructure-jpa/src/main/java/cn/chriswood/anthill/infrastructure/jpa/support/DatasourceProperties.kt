package cn.chriswood.anthill.infrastructure.jpa.support

import cn.chriswood.anthill.infrastructure.jpa.dynamic.DynamicDataSourceProperty
import cn.chriswood.anthill.infrastructure.jpa.multi.MultiDataSourceProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.jpa")
data class DatasourceProperties(
    val enabled: Boolean?,
    val type: String?,
    val dynamic: Map<String, DynamicDataSourceProperty>?,
    val multi: Map<String, MultiDataSourceProperty>?,
)
