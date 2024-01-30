package cn.chriswood.anthill.infrastructure.datasource

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnProperty(
    prefix = "anthill.datasource.multi-jpa",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(MultiJpaDataSourceAutoImport::class)
class DataSourceAutoConfig
