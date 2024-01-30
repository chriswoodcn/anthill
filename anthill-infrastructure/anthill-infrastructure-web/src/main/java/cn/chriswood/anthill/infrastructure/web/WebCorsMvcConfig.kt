package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.json.JacksonConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@AutoConfiguration
@AutoConfigureAfter(JacksonConfig::class)
@ConditionalOnProperty(
    prefix = "anthill.web.cors",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class WebCorsMvcConfig : WebMvcConfigurer {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .maxAge(3600)
        log.info(">>>>>>>>>> init CorsRegistry >>>>>>>>>>")
    }
}
