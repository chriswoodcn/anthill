package cn.chriswood.anthill.infrastructure.datasource

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.EnableTransactionManagement

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.jpa",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableTransactionManagement
@Import(DataSourceAutoImportSelector::class)
class DataSourceAutoConfig
