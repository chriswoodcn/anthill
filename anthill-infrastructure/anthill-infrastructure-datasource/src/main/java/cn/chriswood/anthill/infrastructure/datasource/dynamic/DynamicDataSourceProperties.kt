package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty

data class DynamicDataSourceProperties(
    val dataSources: Map<String, JpaDataSourceProperty?>
)
