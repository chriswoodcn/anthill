package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty
import org.springframework.boot.context.properties.NestedConfigurationProperty

data class DynamicDataSourceProperties(
    val dataSources: Map<String, JpaDataSourceProperty?>
)
