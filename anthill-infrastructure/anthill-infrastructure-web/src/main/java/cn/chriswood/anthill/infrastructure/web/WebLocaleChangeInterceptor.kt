package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.support.RequestContextUtils

class WebLocaleChangeInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val newLocale = HttpRequestUtil.getLocale()
        val localeResolver = RequestContextUtils.getLocaleResolver(request)
        localeResolver?.setLocale(request, response, newLocale)
        return true
    }
}
