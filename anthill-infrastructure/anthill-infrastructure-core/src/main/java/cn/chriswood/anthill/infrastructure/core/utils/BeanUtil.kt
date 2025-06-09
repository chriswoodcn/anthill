package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.annotation.Copy
import cn.hutool.core.bean.BeanUtil
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
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

    inline fun <reified T : Any> copyKBean(source: Any, vararg ignores: KProperty1<T, *>): T {
        val kClass = T::class
        val declaredConstructor = kClass.java.getDeclaredConstructor()
        declaredConstructor.isAccessible = true
        val instance = kClass.objectInstance ?: declaredConstructor.newInstance()
        val sourceFields = getKProperty(source::class)
        val targetFields = if (ignores.isNotEmpty()) {
            getKMutableProperty(T::class).filter {
                !ignores.map { ignore -> ignore.name }.contains(it.name)
            }
        } else {
            getKMutableProperty(T::class)
        }
        val targetInvokes: MutableMap<String, KMutableProperty1<out Any, *>> = mutableMapOf()
        targetFields.forEach {
            it.isAccessible = true
            targetInvokes[it.name] = it
        }
        sourceFields.forEach {
            it.isAccessible = true
            val fieldName = it.name
            val fieldValue = it.getter.call(source)
            if (fieldValue !== null && targetInvokes.keys.contains(fieldName)) {
                try {
                    val tarProperty = targetInvokes[fieldName]
                    if (tarProperty != null) {
                        if (tarProperty.hasAnnotation<Copy>()) {
                            val copyAnno = tarProperty.findAnnotation<Copy>()!!
                            val copyConverter = copyAnno.value.createInstance()
                            tarProperty.setter.call(instance, copyConverter.convert(fieldValue))
                        } else {
                            tarProperty.setter.call(instance, fieldValue)
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
        return instance
    }

    fun getKProperty(clazz: KClass<*>): List<KProperty1<out Any, *>> {
        return clazz.declaredMemberProperties.toList()
    }

    fun getKMutableProperty(clazz: KClass<*>): List<KMutableProperty1<out Any, *>> {
        return clazz.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<out Any, *>>()
            .toList()
    }

    fun <T : Any> copyBean(source: Any, target: Class<T>, vararg ignores: String): T {
        return BeanUtil.copyProperties(source, target, *ignores)
    }

    fun <T : Any> copyBean(source: Any, target: KClass<T>, vararg ignores: KProperty1<T, *>): T {
        return BeanUtil.copyProperties(source, target.java, *(ignores.map { it.name }).toTypedArray())
    }
}
