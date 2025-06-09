package cn.chriswood.anthill.infrastructure.web.annotation.support

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.time.DurationFormatUtils
import org.springframework.validation.BindingResult
import org.springframework.web.multipart.MultipartFile

object AspectUtil {
    fun isFilterObject(o: Any): Boolean {
        val clazz = o.javaClass
        if (clazz.isArray) {
            return clazz.componentType.isAssignableFrom(MultipartFile::class.java)
        } else if (MutableCollection::class.java.isAssignableFrom(clazz)) {
            val collection = o as Collection<*>
            for (value in collection) {
                return value is MultipartFile
            }
        } else if (MutableMap::class.java.isAssignableFrom(clazz)) {
            val map = o as Map<*, *>
            for (value in map.values) {
                return value is MultipartFile
            }
        }
        return (o is MultipartFile || o is HttpServletRequest || o is HttpServletResponse
            || o is BindingResult)
    }

    fun formatMillis(millis: Long): String {
        val str = DurationFormatUtils.formatDuration(millis, " H'h 'm'm 's's'")
            .replace(" 0h", "")
            .replace(" 0m", "")
            .replace(" 0s", "")
            .trim()
        return str.ifBlank { "1s" }
    }
}
