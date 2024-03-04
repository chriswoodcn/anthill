package cn.chriswood.anthill.infrastructure.web.aliyun.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.aliyun-oss-sts")
data class OssStsProperties(
    /**
     * 开关
     */
    val enabled: Boolean = false,
    /**
     * oss sts配置
     */
    val oss: Map<String, OssStsProperty>?,
)
