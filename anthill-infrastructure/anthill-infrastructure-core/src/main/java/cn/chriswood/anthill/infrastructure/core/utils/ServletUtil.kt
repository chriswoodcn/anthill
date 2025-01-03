package cn.chriswood.anthill.infrastructure.core.utils

import cn.hutool.core.convert.Convert
import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.extra.servlet.JakartaServletUtil
import cn.hutool.http.HttpStatus
import jakarta.servlet.ServletRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.MediaType
import org.springframework.util.LinkedCaseInsensitiveMap
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

object ServletUtil {
    /**
     * 获取String参数
     */
    fun getParameter(name: String?): String? {
        return getRequest()?.getParameter(name)
    }

    /**
     * 获取String参数
     */
    fun getParameter(name: String?, defaultValue: String?): String? {
        return Convert.toStr(getRequest()?.getParameter(name), defaultValue)
    }

    /**
     * 获取Integer参数
     */
    fun getParameterToInt(name: String?): Int? {
        return Convert.toInt(getRequest()?.getParameter(name))
    }

    /**
     * 获取Integer参数
     */
    fun getParameterToInt(name: String?, defaultValue: Int?): Int? {
        return Convert.toInt(getRequest()?.getParameter(name), defaultValue)
    }

    /**
     * 获取Boolean参数
     */
    fun getParameterToBool(name: String?): Boolean? {
        return Convert.toBool(getRequest()?.getParameter(name))
    }

    /**
     * 获取Boolean参数
     */
    fun getParameterToBool(name: String?, defaultValue: Boolean?): Boolean? {
        return Convert.toBool(getRequest()?.getParameter(name), defaultValue)
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象[ServletRequest]
     * @return Map
     */
    fun getParams(request: ServletRequest?): Map<String, Array<String>>? {
        val map = request?.parameterMap ?: return null
        return Collections.unmodifiableMap(map)
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象[ServletRequest]
     * @return Map
     */
    fun getParamMap(request: ServletRequest?): Map<String, String>? {
        val params: MutableMap<String, String> = HashMap()
        val requestParams = getParams(request) ?: return null
        for ((key, value) in requestParams) {
            params[key] = StringUtil.join(value.toList(), StringUtil.SEPARATOR)
        }
        return params
    }

    /**
     * 获取request
     */
    fun getRequest(): HttpServletRequest? {
        return getRequestAttributes()?.request
    }

    /**
     * 获取response
     */
    fun getResponse(): HttpServletResponse? {
        return getRequestAttributes()?.response
    }

    /**
     * 获取session
     */
    fun getSession(): HttpSession? {
        return getRequest()?.session
    }

    fun getRequestAttributes(): ServletRequestAttributes? {
        return RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
    }

    fun getHeader(request: HttpServletRequest, name: String?): String? {
        val value = request.getHeader(name) ?: return null
        return if (StringUtil.isEmpty(value)) {
            StringUtil.EMPTY
        } else urlDecode(value)
    }

    fun getHeaders(request: HttpServletRequest): Map<String, String>? {
        val enumeration = request.headerNames ?: return null
        val map: MutableMap<String, String> = LinkedCaseInsensitiveMap()
        while (enumeration.hasMoreElements()) {
            val key = enumeration.nextElement()
            val value = request.getHeader(key)
            map[key] = value
        }
        return map
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    fun renderString(response: HttpServletResponse, string: String?) {
        try {
            response.status = HttpStatus.HTTP_OK
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = StandardCharsets.UTF_8.toString()
            response.writer.print(string)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    fun isAjaxRequest(request: HttpServletRequest): Boolean {
        val accept = request.getHeader("accept")
        if (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return true
        }
        val xRequestedWith = request.getHeader("X-Requested-With")
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true
        }
        val uri = request.requestURI
        if (StringUtil.equalsAnyIgnoreCase(uri, arrayOf(".json", ".xml"))) {
            return true
        }
        val ajax = request.getParameter("__ajax")
        return StringUtil.equalsAnyIgnoreCase(ajax, arrayOf("json", "xml"))
    }

    fun getClientIP(): String? {
        return JakartaServletUtil.getClientIP(getRequest())
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    fun urlEncode(str: String?): String? {
        return URLEncoder.encode(str, StandardCharsets.UTF_8)
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    fun urlDecode(str: String?): String? {
        return URLDecoder.decode(str, StandardCharsets.UTF_8)
    }

    fun getClientIP(request: HttpServletRequest, vararg otherHeaderNames: String): String {
        var headers = arrayOf(
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        )
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = headers.plus(otherHeaderNames)
        }
        return getClientIPByHeader(request, *headers)
    }

    private fun getClientIPByHeader(request: HttpServletRequest, vararg headerNames: String): String {
        val var3: Array<out String> = headerNames
        val var4 = headerNames.size
        var ip: String?
        for (var5 in 0..<var4) {
            val header = var3[var5]
            ip = request.getHeader(header)
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip)
            }
        }
        ip = request.remoteAddr
        return NetUtil.getMultistageReverseProxyIp(ip)
    }
}
