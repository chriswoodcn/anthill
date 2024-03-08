package cn.chriswood.anthill.infrastructure.json.support

import cn.chriswood.anthill.infrastructure.json.annotation.Sensitive
import cn.hutool.extra.spring.SpringUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException

class SensitiveSerializer : JsonSerializer<String?>(), ContextualSerializer {

    private val log = LoggerFactory.getLogger(javaClass)
    private var strategy: SensitiveStrategy? = null
    private var role: String = ""
    private var perm: String = ""
    override fun serialize(value: String?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (null == value || null == gen) return
        try {
            val sensitiveService: SensitiveService = SpringUtil.getBean(SensitiveService::class.java)

            if (strategy != null && sensitiveService.isSensitive(role, perm)) {
                gen.writeString(strategy!!.desensitizer(value))
            } else {
                gen.writeString(value)
            }
        } catch (e: BeansException) {
            log.error("[SensitiveSerializer] sensitiveService not exist, use default string}")
            gen.writeString(value)
        }
    }

    override fun createContextual(prov: SerializerProvider, property: BeanProperty?): JsonSerializer<*> {
        if (property != null) {
            val annotation = property.getAnnotation(Sensitive::class.java) ?: null
            if (annotation != null && String::class.java == property.type.rawClass) {
                strategy = annotation.strategy
                this.role = annotation.role
                this.perm = annotation.perm
                return this
            }
        }
        return prov.findValueSerializer(property?.type, property)
    }
}
