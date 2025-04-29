package cn.chriswood.anthill.infrastructure.push

import cn.chriswood.anthill.infrastructure.push.support.PushProperties
import cn.chriswood.anthill.infrastructure.push.support.PushTemplateAutoImport
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.push",
    name = ["enabled"],
    havingValue = "true"
)
@EnableConfigurationProperties(PushProperties::class)
@Import(PushTemplateAutoImport::class)
class PushConfig
