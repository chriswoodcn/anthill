package cn.chriswood.anthill.infrastructure.json

import cn.chriswood.anthill.infrastructure.json.support.TranslateWorkerManager
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.jackson",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class JacksonConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init TranslateWorkerManager >>>>>>>>>>")
        TranslateWorkerManager.initPool()
    }

    @Bean
    @Primary
//    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer::class)
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer {
        log.debug(">>>>>>>>>> init JacksonConfig >>>>>>>>>>")
        return JacksonUtil.jackson2ObjectMapperBuilderCustomizer()
    }

}
