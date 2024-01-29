package cn.chriswood.anthill.framework.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(cn.hutool.extra.spring.SpringUtil::class)
class HutoolConfig {
}
