package cn.chriswood.anthill.infrastructure.core.utils

import java.lang.reflect.Field
import kotlin.reflect.KProperty1

object BeanUtil {
    /**
     * 使用java反射 配合noArgs和 allOpen插件可以copy data class
     */
    inline fun <reified T : Any> copyBean(source: Any, vararg ignores: KProperty1<T, *>): T {
        val kClass = T::class
        val declaredConstructor = kClass.java.getDeclaredConstructor()
        declaredConstructor.isAccessible = true
        val instance = kClass.objectInstance ?: declaredConstructor.newInstance()
        val sourceFields = getAllFields(source::class.java)
        val targetFields = if (ignores.isNotEmpty()) {
            getAllFields(T::class.java).filter {
                !ignores.map { ignore -> ignore.name }.contains(it.name)
            }
        } else {
            getAllFields(T::class.java)
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
                targetInvokes[fieldName]?.set(instance, fieldValue)
            }
        }
        return instance
    }

    //获取类的所有字段
    fun getAllFields(clazz: Class<*>): MutableList<Field> {
        val initList = clazz.declaredFields.toMutableList()
        val superclass = clazz.superclass
        if (superclass != null) {
            getAllSuperclassFields(superclass, initList)
        }
        return initList
    }

    // 递归获取父类声明的所有字段
    private fun getAllSuperclassFields(clazz: Class<*>, list: MutableList<Field>) {
        val superclass = clazz.superclass
        if (superclass != null) {
            list.addAll(superclass.declaredFields)
            getAllSuperclassFields(superclass, list)
        }
    }
}
