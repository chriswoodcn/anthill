package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.json.JsonConfig
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import cn.chriswood.anthill.infrastructure.web.support.JacksonConfigurationUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@AutoConfiguration
@AutoConfigureAfter(JsonConfig::class)
@ConditionalOnProperty(
    prefix = "anthill.web.jackson",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class WebJacksonConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer::class)
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer {
        log.debug(">>>>>>>>>> init JacksonConfig >>>>>>>>>>")
        val customizer = JacksonConfigurationUtil.jackson2ObjectMapperBuilderCustomizer()
        ///JacksonUtil替换ObjectMapper
        val builder = Jackson2ObjectMapperBuilder()
        customizer.customize(builder)
        JacksonUtil.replaceObjectMapper(builder.build())
        return customizer
    }
}
