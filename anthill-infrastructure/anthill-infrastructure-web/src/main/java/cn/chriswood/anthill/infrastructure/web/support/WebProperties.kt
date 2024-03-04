package cn.chriswood.anthill.infrastructure.web.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.web")
data class WebProperties(
    val captcha: Enabled?,
    val invoke: Enabled?,
    val xss: Enabled?
) {
    data class Enabled(
        val enabled: Boolean = false
    )
}
