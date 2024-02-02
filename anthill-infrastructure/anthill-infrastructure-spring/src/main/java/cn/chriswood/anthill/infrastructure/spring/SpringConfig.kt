package cn.chriswood.anthill.infrastructure.spring

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@EnableConfigurationProperties(ApplicationConfig::class)
@Import(
    cn.hutool.extra.spring.SpringUtil::class
)
class SpringConfig
