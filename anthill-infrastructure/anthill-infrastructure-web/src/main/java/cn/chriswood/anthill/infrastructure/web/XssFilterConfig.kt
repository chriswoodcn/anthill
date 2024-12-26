package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.chriswood.anthill.infrastructure.web.support.XssFilter
import cn.chriswood.anthill.infrastructure.web.support.XssProperties
import jakarta.servlet.DispatcherType
import jakarta.servlet.Filter
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(XssProperties::class)
@ConditionalOnProperty(
    prefix = "anthill.web.xss",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class XssFilterConfig(
    val xssProperties: XssProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun xssFilterRegistration(): FilterRegistrationBean<Filter> {
        val registration: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        registration.setDispatcherTypes(DispatcherType.REQUEST)
        registration.setFilter(XssFilter())
        if (StringUtil.isNotBlank(xssProperties.urlPatterns)) {
            registration.addUrlPatterns(*StringUtil.split(xssProperties.urlPatterns!!, ','))
        }
        registration.setName("xssFilter")
        registration.order = FilterRegistrationBean.HIGHEST_PRECEDENCE
        val initParameters: MutableMap<String, String> = HashMap()
        if (StringUtil.isNotBlank(xssProperties.excludes)) {
            initParameters["excludes"] = xssProperties.excludes!!
        }
        registration.setInitParameters(initParameters)
        log.debug(">>>>>>>>>> init XssFilterConfig >>>>>>>>>>")
        return registration
    }
}
