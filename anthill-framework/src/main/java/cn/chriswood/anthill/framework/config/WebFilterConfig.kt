package cn.chriswood.anthill.framework.config

import cn.chriswood.anthill.framework.interceptor.RepeatableFilter
import cn.chriswood.anthill.framework.interceptor.XssFilter
import cn.chriswood.anthill.framework.interceptor.XssProperties
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import jakarta.servlet.DispatcherType
import jakarta.servlet.Filter
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(XssProperties::class)
class WebFilterConfig(
    val xssProperties: XssProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnProperty(
        prefix = "xss", name = ["enabled"], havingValue = "true"
    )
    fun xssFilterRegistration(): FilterRegistrationBean<Filter> {
        val registration: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        registration.setDispatcherTypes(DispatcherType.REQUEST)
        registration.setFilter(XssFilter())
        registration.addUrlPatterns(*StringUtil.split(xssProperties.urlPatterns!!, ','))
        registration.setName("xssFilter")
        registration.order = FilterRegistrationBean.HIGHEST_PRECEDENCE
        val initParameters: MutableMap<String, String> = HashMap()
        initParameters["excludes"] = xssProperties.excludes!!
        initParameters["enabled"] = xssProperties.enabled!!
        registration.setInitParameters(initParameters)
        log.info(">>>>>>>>>> init xssFilter >>>>>>>>>>")
        return registration
    }

    @Bean
    fun repeatableFilterRegistration(): FilterRegistrationBean<Filter> {
        val registration: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        registration.setFilter(RepeatableFilter())
        registration.addUrlPatterns("/*")
        registration.setName("repeatableFilter")
        registration.order = FilterRegistrationBean.LOWEST_PRECEDENCE
        log.info(">>>>>>>>>> init repeatableFilter >>>>>>>>>>")
        return registration
    }
}
