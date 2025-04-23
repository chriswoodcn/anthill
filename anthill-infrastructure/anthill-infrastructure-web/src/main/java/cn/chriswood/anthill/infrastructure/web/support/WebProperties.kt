package cn.chriswood.anthill.infrastructure.web.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.web")
data class WebProperties(
    val invoke: Enabled?,
    val cors: Enabled?,
    val captcha: Enabled?,
    val jackson: Enabled?,
) {
    data class Enabled(
        /**
         * 开关
         */
        val enabled: Boolean?,
    )
}
