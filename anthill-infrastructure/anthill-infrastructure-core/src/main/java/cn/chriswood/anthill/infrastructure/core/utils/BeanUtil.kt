package cn.chriswood.anthill.infrastructure.core.utils

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object BeanUtil {
    inline fun <reified T : Any> copyBean(source: Any): T {
        val kClass = T::class
        val declaredConstructor = kClass.java.getDeclaredConstructor()
        declaredConstructor.isAccessible = true
        val instance = kClass.objectInstance ?: declaredConstructor.newInstance()
        val sourceFields = source::class.memberProperties
        val targetFields = instance::class.memberProperties
        val targetFieldInvokes: MutableMap<String, KProperty1<out T, *>> = mutableMapOf()
        targetFields.forEach {
            it.isAccessible = true
            targetFieldInvokes[it.name] = it
        }
        if (targetFieldInvokes.isEmpty()) return instance
        sourceFields.forEach() {
            it.isAccessible = true
            val fieldName = it.name
            val fieldValue = it.getter.call(source)
            if (fieldValue != null && targetFieldInvokes.keys.contains(fieldName)) {
                val invoke = targetFieldInvokes[it.name]
                try {
                    val kp = invoke as KMutableProperty1<*, *>
                    kp.setter.call(instance, fieldValue)
                } catch (_: Exception) {

                }
            }
        }
        return instance
    }
}
