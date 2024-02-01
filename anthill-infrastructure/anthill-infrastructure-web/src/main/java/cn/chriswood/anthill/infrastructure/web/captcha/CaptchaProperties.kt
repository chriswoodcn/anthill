package cn.chriswood.anthill.infrastructure.web.captcha

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.web.captcha")
data class CaptchaProperties(
    /**
     * 开关
     */
    var enable: Boolean? = null,

    /**
     * 验证码类型
     */
    val type: CaptchaType? = null,

    /**
     * 验证码类别
     */
    val category: CaptchaCategory? = null,

    /**
     * 数字验证码位数
     */
    val numberLength: Int? = null,

    /**
     * 字符验证码长度
     */
    val charLength: Int? = null,
)
