package cn.chriswood.anthill.infrastructure.core.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@EnableConfigurationProperties(
    ApplicationConfig::class,
    ExceptionConfig::class
)
@Import(
    cn.hutool.extra.spring.SpringUtil::class
)
class SpringConfig
