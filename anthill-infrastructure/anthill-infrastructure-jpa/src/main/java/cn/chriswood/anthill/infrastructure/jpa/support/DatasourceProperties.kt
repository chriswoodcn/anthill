package cn.chriswood.anthill.infrastructure.jpa.support

import cn.chriswood.anthill.infrastructure.jpa.dynamic.DynamicDataSourceProperty
import cn.chriswood.anthill.infrastructure.jpa.multi.MultiDataSourceProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.jpa")
data class DatasourceProperties(
    /**
     * 开关
     */
    val enabled: Boolean?,
    /**
     * 类型 dynamic动态数据源 multi多数据源
     */
    val type: String?,
    /**
     * dynamic动态数据源配置
     */
    val dynamic: Map<String, DynamicDataSourceProperty>?,
    /**
     * multi多数据源配置
     */
    val multi: Map<String, MultiDataSourceProperty>?,
)
