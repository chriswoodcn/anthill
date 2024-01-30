package cn.chriswood.anthill.infrastructure.spring

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(
    ApplicationConfig::class,
    cn.hutool.extra.spring.SpringUtil::class
)
class SpringConfig
