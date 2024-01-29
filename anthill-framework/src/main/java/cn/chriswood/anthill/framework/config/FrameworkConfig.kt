package cn.chriswood.anthill.framework.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ApplicationConfig::class,
    HutoolConfig::class
)
class FrameworkConfig {
}
