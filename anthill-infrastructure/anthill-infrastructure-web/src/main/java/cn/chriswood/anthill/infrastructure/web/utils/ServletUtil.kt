package cn.chriswood.anthill.infrastructure.web.utils

import cn.hutool.extra.servlet.JakartaServletUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object ServletUtil {
    fun getRequestAttributes(): ServletRequestAttributes? {
        return try {
            val attributes = RequestContextHolder.getRequestAttributes()
            attributes as ServletRequestAttributes?
        } catch (e: java.lang.Exception) {
            null
        }
    }
    fun getRequest(): HttpServletRequest? {
        return try {
            getRequestAttributes()?.request
        } catch (e: Exception) {
            null
        }
    }

    fun getClientIP(): String {
        return JakartaServletUtil.getClientIP(getRequest())
    }
}
