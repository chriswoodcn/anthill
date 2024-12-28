package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor


class WebLocaleChangeInterceptor : LocaleChangeInterceptor() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val newLocale = HttpRequestUtil.getLocale(request)
        LocaleContextHolder.setLocale(newLocale)
        return true
    }
}
