package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.json.JacksonConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@AutoConfiguration
@AutoConfigureAfter(JacksonConfig::class)
class WebLocaleMvcConfig : WebMvcConfigurer {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(WebLocaleChangeInterceptor());
        log.debug(">>>>>>>>>> init WebLocaleChangeInterceptor >>>>>>>>>>")
        super.addInterceptors(registry)
    }
}
