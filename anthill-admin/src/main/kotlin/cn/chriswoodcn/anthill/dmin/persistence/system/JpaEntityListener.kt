package com.taotao.bmm.persistence.system

import jakarta.persistence.PrePersist
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JpaEntityListener {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PrePersist
    fun prePersist(entity: Any) {
        log.debug("prePersist handler set defaultValue")
        val declaredFields = entity::class.java.declaredFields
        declaredFields.forEach {
            it.isAccessible = true
            if (it.isAnnotationPresent(DefaultValue::class.java)) {
                val annotation = it.getDeclaredAnnotation(DefaultValue::class.java)
                if (null == it.get(entity)) {
                    if (it.declaringClass == String::class.java
                        || it.declaringClass == Int::class.java
                    )
                        it.set(entity, annotation.value)
                }
            }
        }
        log.debug("prePersist set defaultValue {}", entity)
    }
}