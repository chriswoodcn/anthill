package cn.chriswood.anthill.infrastructure.web.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

object HttpRequestUtil : HttpRequestUtilInterface {
    const val LanguageTag = "language"
    const val DefaultLang = "en"
    const val DeviceTag = "device"
    const val EndpointTag = "endpoint"
    const val DefaultTag = "unknown"
    fun getRequest(): HttpServletRequest {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    }

    override fun getLang(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), LanguageTag) ?: return DefaultLang
        if (headerIgnoreCase.indexOf('_') > -1) {
            return headerIgnoreCase.split('_')[0]
        }
        return headerIgnoreCase
    }

    fun getLang(request: HttpServletRequest): String {
        val headerIgnoreCase = getHeaderIgnoreCase(request, LanguageTag) ?: return DefaultLang
        if (headerIgnoreCase.indexOf('_') > -1) {
            return headerIgnoreCase.split('_')[0]
        }
        return headerIgnoreCase
    }

    override fun getLocale(): Locale {
        return Locale(getLang())
    }

    fun getLocale(request: HttpServletRequest): Locale {
        return Locale(getLang(request))
    }

    override fun getDevice(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), DeviceTag)
        return headerIgnoreCase ?: DefaultTag
    }

    override fun getEndpoint(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), EndpointTag)
        return headerIgnoreCase ?: DefaultTag
    }

    fun getHeaderIgnoreCase(request: HttpServletRequest, nameIgnoreCase: String?): String? {
        val names = request.headerNames
        var name: String?
        while (names.hasMoreElements()) {
            name = names.nextElement()
            if (name != null && name.equals(nameIgnoreCase, ignoreCase = true)) {
                return request.getHeader(name)
            }
        }
        return null
    }
}
