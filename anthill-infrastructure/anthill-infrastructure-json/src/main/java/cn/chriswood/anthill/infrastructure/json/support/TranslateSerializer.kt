package cn.chriswood.anthill.infrastructure.json.support

import cn.chriswood.anthill.infrastructure.json.annotation.Translate
import cn.hutool.extra.spring.SpringUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import org.slf4j.LoggerFactory

class TranslateSerializer : JsonSerializer<String>(), ContextualSerializer {
    private val log = LoggerFactory.getLogger(javaClass)
    private var annotation: Translate? = null
    override fun serialize(value: String?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        SpringUtil.getBeansOfType(TranslateWorker::class.java)
    }

    override fun createContextual(prov: SerializerProvider, property: BeanProperty?): JsonSerializer<*> {
        if (property != null) {
            val annotation = property.getAnnotation(Translate::class.java) ?: null
            if (annotation != null) {
                this.annotation = annotation;
                return this
            }
        }
        return prov.findValueSerializer(property?.type, property)
    }

    private fun getAvailableTranslateWorker(key: String): TranslateWorker? {
        val workerPool = SpringUtil.getBeansOfType(TranslateWorker::class.java)
        val workerList = workerPool.map { it.value }.toList()
        val workerMap: MutableMap<String, TranslateWorker> = mutableMapOf()
        workerList.forEach {
            val annotation = it.javaClass.getAnnotation(Translate::class.java) ?: null
            if (null == annotation)
                log.warn(
                    "{} not mark Translate annotation, application can not use it to translate.",
                    it.javaClass.name
                )
            if (annotation!!.key.isNotEmpty())
                workerMap.plus(annotation.key to it)
        }
        return null
    }
}
