package cn.chriswood.anthill.infrastructure.core.utils

import java.lang.reflect.Field

object BeanUtil {
    /**
     * 使用java反射 配合noArgs和 allOpen插件可以copy data class
     */
    inline fun <reified T : Any> copyBean(source: Any): T {
        val kClass = T::class
        val declaredConstructor = kClass.java.getDeclaredConstructor()
        declaredConstructor.isAccessible = true
        val instance = kClass.objectInstance ?: declaredConstructor.newInstance()
        val sourceFields = source::class.java.declaredFields
        val targetFields = instance::class.java.declaredFields
        val targetInvokes: MutableMap<String, Field> = mutableMapOf()
        targetFields.forEach {
            val declaredField = instance::class.java.getDeclaredField(it.name)
            declaredField.isAccessible = true
            targetInvokes[it.name] = declaredField
        }
        sourceFields.forEach {
            it.isAccessible = true
            val fieldName = it.name
            val fieldValue = it.get(source)
            if (fieldValue !== null && targetInvokes.keys.contains(fieldName)) {
                targetInvokes[fieldName]?.set(instance, fieldValue)
            }
        }
        return instance
    }
}
