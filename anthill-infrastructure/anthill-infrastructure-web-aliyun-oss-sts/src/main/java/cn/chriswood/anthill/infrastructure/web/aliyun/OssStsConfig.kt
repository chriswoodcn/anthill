package cn.chriswood.anthill.infrastructure.web.aliyun

import cn.chriswood.anthill.infrastructure.web.aliyun.support.OssStsProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.aliyun-oss-sts",
    name = ["enabled"],
    havingValue = "true"
)
@EnableConfigurationProperties(OssStsProperties::class)
class OssStsConfig
