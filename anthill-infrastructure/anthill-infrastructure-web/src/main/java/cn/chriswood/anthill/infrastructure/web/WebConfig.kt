package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.support.WebProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration
@EnableConfigurationProperties(WebProperties::class)
class WebConfig
