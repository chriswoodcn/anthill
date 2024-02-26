package cn.chriswood.anthill.infrastructure.web.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.web.xss")
data class XssProperties(
    /**
     * 过滤开关
     */
    val enabled: String?,
    /**
     * 排除链接（多个用逗号分隔）
     */
    val excludes: String?,
    /**
     * 匹配链接
     */
    val urlPatterns: String?,
)