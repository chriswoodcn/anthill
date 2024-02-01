package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.common.JpaTypeProperty
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.jpa",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties(JpaTypeProperty::class)
@Import(DataSourceAutoImportSelector::class)
class DataSourceAutoConfig
