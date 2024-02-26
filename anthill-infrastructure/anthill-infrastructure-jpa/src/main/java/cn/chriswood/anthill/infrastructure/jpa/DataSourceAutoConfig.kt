package cn.chriswood.anthill.infrastructure.jpa

import cn.chriswood.anthill.infrastructure.jpa.dynamic.DynamicDataSourceAspect
import cn.chriswood.anthill.infrastructure.jpa.support.DataSourceAutoImportSelector
import cn.chriswood.anthill.infrastructure.jpa.support.DatasourceProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration::class)
@ConditionalOnProperty(
    prefix = "anthill.jpa",
    name = ["enabled"],
    havingValue = "true",
)
@EnableConfigurationProperties(DatasourceProperties::class)
@Import(DataSourceAutoImportSelector::class, DynamicDataSourceAspect::class)
class DataSourceAutoConfig
