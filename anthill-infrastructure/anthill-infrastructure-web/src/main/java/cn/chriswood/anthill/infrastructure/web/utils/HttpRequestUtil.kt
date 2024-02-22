package cn.chriswood.anthill.infrastructure.web.utils

import cn.chriswood.anthill.infrastructure.core.enums.DeviceType
import cn.chriswood.anthill.infrastructure.core.enums.EndpointType
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

object HttpRequestUtil {
    const val LanguageTag = "content-language"
    const val DefaultLang = "en"
    const val DeviceTag = "device"
    const val EndpointTag = "endpoint"
    const val DefaultTag = "unknown"
    private fun getRequest(): HttpServletRequest {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    }

    fun getLang(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), LanguageTag) ?: return DefaultLang
        if (headerIgnoreCase.indexOf('_') > -1) {
            return headerIgnoreCase.split('_')[0]
        }
        return headerIgnoreCase
    }

    fun getLocale(): Locale {
        return Locale(getLang())
    }

    fun getDevice(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), DeviceTag)
        return headerIgnoreCase ?: DefaultTag
    }

    fun getDeviceEnum(): DeviceType {
        return DeviceType.getEnumByCode(getDevice())
    }

    fun getEndpoint(): String {
        val headerIgnoreCase = getHeaderIgnoreCase(getRequest(), EndpointTag)
        return headerIgnoreCase ?: DefaultTag
    }

    fun getEndpointEnum(): EndpointType {
        return EndpointType.getEnumByCode(getEndpoint())
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
