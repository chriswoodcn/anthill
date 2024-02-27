package cn.chriswood.anthill.infrastructure.mail.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.mail")
data class MailProperties(
    /**
     * 过滤开关
     */
    var enabled: Boolean?,

    /**
     * SMTP服务器域名
     */

    val host: String,

    /**
     * SMTP服务端口
     */

    val port: Int,

    /**
     * 是否需要用户名密码验证
     */

    val auth: Boolean,

    /**
     * 用户名
     */

    val user: String,

    /**
     * 密码
     */

    val pass: String,

    /**
     * 发送方，遵循RFC-822标准
     */

    val from: String,

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */

    val starttlsEnable: Boolean,

    /**
     * 使用 SSL安全连接
     */

    val sslEnable: Boolean,

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时
     */

    val timeout: Long?,

    /**
     * Socket连接超时值，单位毫秒，缺省值不超时
     */
    val connectionTimeout: Long?,
)
