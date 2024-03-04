package cn.chriswood.anthill.infrastructure.mail.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.mail")
data class MailProperties(
    /**
     * 开关
     */
    val enabled: Boolean?,
    /**
     * 内置邮箱账号
     */
    val account: List<MailAccountProperty>?,
)
