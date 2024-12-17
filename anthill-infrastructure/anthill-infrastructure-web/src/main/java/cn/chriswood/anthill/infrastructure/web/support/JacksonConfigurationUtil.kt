package cn.chriswood.anthill.infrastructure.web.support

import cn.chriswood.anthill.infrastructure.json.support.TranslationSerializerModifier
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.util.*

object JacksonConfigurationUtil {
    interface ApplyJacksonConfigurations {
        fun apply(builder: Jackson2ObjectMapperBuilder)
    }

    object DefaultJacksonConfigurationsApplier : ApplyJacksonConfigurations {
        override fun apply(builder: Jackson2ObjectMapperBuilder) {
            builder.serializers()
            builder.serializationInclusion(JsonInclude.Include.ALWAYS)
            builder.failOnUnknownProperties(false)
            builder.failOnEmptyBeans(false)
            //关闭一些序列化特性
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            builder.featuresToDisable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
            //关闭一些反序列化特性
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            builder.simpleDateFormat(JacksonUtil.dateTimeFormat)
            builder.timeZone(TimeZone.getDefault())

            builder.postConfigurer {
                it.setSerializerFactory(
                    it.serializerFactory
                        .withSerializerModifier(TranslationSerializerModifier())
                )
            }
        }
    }

    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return jackson2ObjectMapperBuilderCustomizer(null)
    }

    fun jackson2ObjectMapperBuilderCustomizer(applier: ApplyJacksonConfigurations?): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
            //builder配置
            builder.modules(JacksonUtil.buildJavaTimeModule(), JacksonUtil.buildBigNumberModule())
            applier?.apply(builder) ?: DefaultJacksonConfigurationsApplier.apply(builder)
        }
    }

}
