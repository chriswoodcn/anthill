package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAspect
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration::class)
@ConditionalOnProperty(
    prefix = "anthill.jpa",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(DataSourceAutoImportSelector::class, DynamicDataSourceAspect::class)
class DataSourceAutoConfig
