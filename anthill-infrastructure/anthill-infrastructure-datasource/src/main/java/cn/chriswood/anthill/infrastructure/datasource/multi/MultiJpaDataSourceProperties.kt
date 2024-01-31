package cn.chriswood.anthill.infrastructure.datasource.multi

import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty

data class MultiJpaDataSourceProperties(
    val dataSources: Map<String, JpaDataSourceProperty?>
)
