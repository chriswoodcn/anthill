package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.support.RequestContextUtils

class WebLocaleChangeInterceptor : LocaleChangeInterceptor() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val newLocale = HttpRequestUtil.getLocale(request)
        val localeResolver = RequestContextUtils.getLocaleResolver(request)
            ?: throw IllegalStateException(
                "No LocaleResolver found: not in a DispatcherServlet request?"
            )
        try {
            localeResolver.setLocale(request, response, newLocale)
        } catch (ex: IllegalArgumentException) {
            if (isIgnoreInvalidLocale) {
                if (logger.isDebugEnabled) {
                    logger.debug("Ignoring invalid locale value [" + newLocale + "]: " + ex.message)
                }
            } else {
                throw ex
            }
        }
        // Proceed in any case.
        return true
    }
}
