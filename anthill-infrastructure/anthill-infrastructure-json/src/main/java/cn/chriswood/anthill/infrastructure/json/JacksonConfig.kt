package cn.chriswood.anthill.infrastructure.json

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer::class)
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer {
        log.info(">>>>>>>>>> init JacksonConfig >>>>>>>>>>")
        return JacksonUtil.jackson2ObjectMapperBuilderCustomizer()
    }
}
