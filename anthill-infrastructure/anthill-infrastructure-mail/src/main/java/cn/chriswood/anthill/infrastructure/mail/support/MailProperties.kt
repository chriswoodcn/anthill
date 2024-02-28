package cn.chriswood.anthill.infrastructure.mail.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.mail")
data class MailProperties(
    val enabled: Boolean?,
    val account: List<MailAccountProperties>?,
)
