package cn.chriswood.anthill.infrastructure.core.utils

import org.springframework.beans.BeanUtils

object BeanUtil {
    inline fun <reified T : Any> copyBean(source: Any): T {
        val mCreate = T::class.java.getDeclaredConstructor()
        mCreate.isAccessible = true
        val newInstance = mCreate.newInstance()
        BeanUtils.copyProperties(source, newInstance)
        return newInstance
    }
}
