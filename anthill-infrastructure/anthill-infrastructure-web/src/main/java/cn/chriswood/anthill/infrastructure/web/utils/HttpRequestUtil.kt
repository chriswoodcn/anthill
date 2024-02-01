package cn.chriswood.anthill.infrastructure.web.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

object HttpRequestUtil {
    const val LanguageTag = "language"
    const val DefaultLang = "en"
    const val DeviceTag = "device"
    const val SignTypeTag = "signtype"
    const val DefaultTag = "anonymous"
    private fun getRequest(): HttpServletRequest {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    }

    fun getLang(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), LanguageTag)
        return headerIgnoreCase ?: DefaultLang
    }

    fun getLocale(): Locale {
        return Locale(getLang())
    }

    fun getDevice(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), DeviceTag)
        return headerIgnoreCase ?: DefaultTag
    }

    fun getSignType(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), SignTypeTag)
        return headerIgnoreCase ?: DefaultTag
    }

    private fun getHeaderIgnoreCase(request: HttpServletRequest, nameIgnoreCase: String?): String? {
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
