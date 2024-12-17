package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.json.JsonConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@AutoConfiguration
@AutoConfigureAfter(JsonConfig::class)
@ConditionalOnProperty(
    prefix = "anthill.web.cors",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class WebCorsFilterConfig {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addAllowedOriginPattern("*")
        config.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        log.debug(">>>>>>>>>> init WebCorsFilterConfig >>>>>>>>>>")
        return CorsFilter(source)
    }
}
