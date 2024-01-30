package cn.chriswood.anthill.infrastructure.datasource

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Import

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.datasource",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(DataSourceAutoImportSelector::class)
class DataSourceAutoConfig
