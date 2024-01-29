package cn.chriswood.anthill.datasource

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(DataSourceAutoImport::class)
class DataSourceAutoConfig
