package cn.chriswood.anthill.infrastructure.web.socket.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.web.socket")
data class WebSocketProperties(
    /**
     * 是否开启
     */
    private var enabled: Boolean = false,

    /**
     * 路径
     */
    val path: String = "/websocket",

    /**
     * 设置访问源地址
     */
    val allowedOrigins: String = "*",
)
