package cn.chriswood.anthill.infrastructure.json

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.jackson",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class JacksonConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer::class)
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer {
        log.debug(">>>>>>>>>> init JacksonConfig >>>>>>>>>>")
        return JacksonUtil.jackson2ObjectMapperBuilderCustomizer()
    }
}
