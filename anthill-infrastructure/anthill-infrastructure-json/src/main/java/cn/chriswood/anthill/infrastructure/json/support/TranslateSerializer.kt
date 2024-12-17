package cn.chriswood.anthill.infrastructure.json.support

import cn.chriswood.anthill.infrastructure.json.annotation.Translate
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.ReflectUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import org.slf4j.LoggerFactory

class TranslateSerializer : JsonSerializer<Any?>(), ContextualSerializer {
    private val log = LoggerFactory.getLogger(javaClass)
    private var annotation: Translate? = null
    override fun serialize(value: Any?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (null == value || null == gen) return
        if (null != annotation && annotation!!.key.isNotBlank()) {
            val worker = TranslateWorkerManager.getAvailableTranslateWorker(annotation!!.key)
            if (null != worker) {
                val fieldValue = ReflectUtil.getFieldValue(gen.getCurrentValue(), annotation!!.mapper) ?: null
                // 如果为 null 直接写出
                if (ObjectUtil.isNull(fieldValue)) {
                    gen.writeNull()
                    return
                }
                val translatedValue: Any? = worker.translation(fieldValue, annotation!!.custom)
                gen.writeObject(translatedValue)
            }
        }
        gen.writeObject(value)
    }

    override fun createContextual(prov: SerializerProvider, property: BeanProperty?): JsonSerializer<*> {
        if (property != null) {
            val annotation = property.getAnnotation(Translate::class.java) ?: null
            if (annotation != null) {
                this.annotation = annotation
                return this
            }
        }
        return prov.findValueSerializer(property?.type, property)
    }
}
