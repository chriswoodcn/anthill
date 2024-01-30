package cn.chriswood.anthill.infrastructure.datasource

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.validation.annotation.Validated

@Configuration
@ConditionalOnProperty(
    prefix = "anthill.datasource",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(DataSourceAutoImportSelector::class)
class DataSourceAutoConfig
