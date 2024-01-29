package cn.chriswood.anthill.framework.config

import cn.chriswood.anthill.framework.interceptor.WebInvokeTimeInterceptor
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addWebRequestInterceptor(WebInvokeTimeInterceptor())
        log.info(">>>>>>>>>> init WebInvokeTimeInterceptor >>>>>>>>>>")
        super.addInterceptors(registry)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            // 默认 GET POST HEAD开放
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600)
        log.info(">>>>>>>>>> init CorsRegistry >>>>>>>>>>")
    }
}
