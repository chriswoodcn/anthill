package cn.chriswood.anthill.infrastructure.web.aliyun.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.aliyun-oss-sts")
data class OssStsProperties(
    val enabled: Boolean = false,
    val oss: Map<String, OssStsProperty>?,
)
