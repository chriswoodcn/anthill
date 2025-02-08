package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.annotation.Copy
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

object BeanUtil {
    /**
     * 使用java反射 配合noArgs和 allOpen插件可以copy data class
     */
    inline fun <reified T : Any> copyBean(source: Any, vararg ignores: KProperty1<T, *>): T {
        val kClass = T::class
        val declaredConstructor = kClass.java.getDeclaredConstructor()
        declaredConstructor.isAccessible = true
        val instance = kClass.objectInstance ?: declaredConstructor.newInstance()
        val sourceFields = getAllFields(source::class)
        val targetFields = if (ignores.isNotEmpty()) {
            getAllFields(T::class).filter {
                !ignores.map { ignore -> ignore.name }.contains(it.name)
            }
        } else {
            getAllFields(T::class)
        }
        val targetInvokes: MutableMap<String, Field> = mutableMapOf()
        targetFields.forEach {
            it.isAccessible = true
            targetInvokes[it.name] = it
        }
        sourceFields.forEach {
            it.isAccessible = true
            val fieldName = it.name
            val fieldValue = it.get(source)
            if (fieldValue !== null && targetInvokes.keys.contains(fieldName)) {
                try {
                    val tarField = targetInvokes[fieldName]
                    if (tarField != null) {
                        if (tarField.isAnnotationPresent(Copy::class.java)) {
                            val copyAnno = tarField.getAnnotation(Copy::class.java)
                            val copyConverter = copyAnno.value.java.getDeclaredConstructor().newInstance()
                            tarField.set(instance, copyConverter.convert(fieldValue))
                        } else {
                            tarField.set(instance, fieldValue)
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
        return instance
    }

    //获取类的所有字段
    fun getAllFields(clazz: KClass<*>): MutableList<Field> {
        return clazz.memberProperties.mapNotNull { it.javaField }.toMutableList()
    }
}
