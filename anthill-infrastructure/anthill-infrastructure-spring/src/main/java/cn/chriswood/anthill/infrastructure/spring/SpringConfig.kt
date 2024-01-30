package cn.chriswood.anthill.infrastructure.spring

import cn.chriswood.anthill.framework.config.ApplicationConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ApplicationConfig::class,
    cn.hutool.extra.spring.SpringUtil::class
)
class SpringConfig
