package cn.chriswood.anthill.infrastructure.push.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.push")
data class PushProperties(
    /**
     * 开关
     */
    val enabled: Boolean?,
    /**
     * 内置邮箱账号
     */
    val accounts: List<PushTemplateProperty>?
)
