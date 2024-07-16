package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*

class WebLocaleResolver : AcceptHeaderLocaleResolver() {
    private var webLocale: Locale = Locale("en")
    override fun resolveLocale(request: HttpServletRequest): Locale {
        webLocale = getLocale(request)
        return webLocale
    }

    override fun setLocale(request: HttpServletRequest, response: HttpServletResponse?, locale: Locale?) {
        webLocale = locale ?: getLocale(request)
    }

    private fun getLocale(request: HttpServletRequest): Locale {
        val headerIgnoreCase = HttpRequestUtil.getHeaderIgnoreCase(
            request,
            HttpRequestUtil.LanguageTag
        ) ?: return Locale(HttpRequestUtil.DefaultLang)
        if (headerIgnoreCase.indexOf('_') > -1) {
            return Locale(headerIgnoreCase.split('_')[0])
        }
        return Locale(headerIgnoreCase)
    }
}
