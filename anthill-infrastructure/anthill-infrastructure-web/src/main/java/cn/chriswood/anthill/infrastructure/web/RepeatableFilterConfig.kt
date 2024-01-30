package cn.chriswood.anthill.infrastructure.web

import jakarta.servlet.Filter
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.webinvoke",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class RepeatableFilterConfig {
    private val log = LoggerFactory.getLogger(javaClass)

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
