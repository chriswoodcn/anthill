package cn.chriswood.anthill.infrastructure.web.config

import cn.chriswood.anthill.infrastructure.json.JacksonConfig
import cn.chriswood.anthill.infrastructure.web.interceptor.WebInvokeTimeInterceptor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@AutoConfiguration
@AutoConfigureAfter(JacksonConfig::class)
@ConditionalOnProperty(
    prefix = "anthill.web.invoke",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class WebInvokeMvcConfig : WebMvcConfigurer {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addWebRequestInterceptor(WebInvokeTimeInterceptor())
        log.info(">>>>>>>>>> init WebInvokeTimeInterceptor >>>>>>>>>>")
        super.addInterceptors(registry)
    }
}
