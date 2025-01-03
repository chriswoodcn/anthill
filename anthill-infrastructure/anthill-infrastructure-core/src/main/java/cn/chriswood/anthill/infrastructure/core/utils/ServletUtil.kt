package cn.chriswood.anthill.infrastructure.core.utils

import cn.hutool.core.convert.Convert
import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.extra.servlet.JakartaServletUtil
import cn.hutool.http.HttpStatus
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import jakarta.servlet.ServletRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.apache.logging.log4j.util.Strings
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

    fun getIpLocation(ipv4: String?): String? {
        if (ipv4.isNullOrBlank()) return null
        if (checkIfInternalIp(ipv4ToByteArray(ipv4))) return "InternalIp"
        try {
            val rspStr: String = HttpUtil.get("https://whois.pconline.com.cn/ipJson.jsp?ip=${ipv4}&json=true", 3000)
            if (rspStr.isBlank()) {
                return "Unknown"
            }
            val obj = JSONUtil.parseObj(rspStr)
            val region: String = obj.getStr("pro") ?: Strings.EMPTY
            val city: String = obj.getStr("city") ?: Strings.EMPTY
            val res = String.format("%s %s", region, city)
            return res.ifBlank { "Unknown" }
        } catch (e: Exception) {
            return "Unknown"
        }
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
        val res = NetUtil.getMultistageReverseProxyIp(ip)
        return if ("0:0:0:0:0:0:0:1" == res) "127.0.0.1" else res
    }

    fun ipv4ToByteArray(text: String): ByteArray? {
        if (text.isEmpty()) {
            return null
        }
        val bytes = ByteArray(4)
        val elements = text.split("\\.".toRegex()).toTypedArray()
        try {
            var l: Long
            var i: Int
            when (elements.size) {
                1 -> {
                    l = elements[0].toLong()
                    if (l < 0L || l > 4294967295L) {
                        return null
                    }
                    bytes[0] = (l shr 24 and 0xFFL).toInt().toByte()
                    bytes[1] = (l and 0xFFFFFFL shr 16 and 0xFFL).toInt().toByte()
                    bytes[2] = (l and 0xFFFFL shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                2 -> {
                    l = elements[0].toInt().toLong()
                    if (l < 0L || l > 255L) {
                        return null
                    }
                    bytes[0] = (l and 0xFFL).toInt().toByte()
                    l = elements[1].toInt().toLong()
                    if (l < 0L || l > 16777215L) {
                        return null
                    }
                    bytes[1] = (l shr 16 and 0xFFL).toInt().toByte()
                    bytes[2] = (l and 0xFFFFL shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                3 -> {
                    i = 0
                    while (i < 2) {
                        l = elements[i].toInt().toLong()
                        if (l < 0L || l > 255L) {
                            return null
                        }
                        bytes[i] = (l and 0xFFL).toInt().toByte()
                        ++i
                    }
                    l = elements[2].toInt().toLong()
                    if (l < 0L || l > 65535L) {
                        return null
                    }
                    bytes[2] = (l shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                4 -> {
                    i = 0
                    while (i < 4) {
                        l = elements[i].toInt().toLong()
                        if (l < 0L || l > 255L) {
                            return null
                        }
                        bytes[i] = (l and 0xFFL).toInt().toByte()
                        ++i
                    }
                }

                else -> return null
            }
        } catch (e: NumberFormatException) {
            return null
        }
        return bytes
    }

    fun checkIfInternalIp(addr: ByteArray?): Boolean {
        if (addr == null || addr.size < 2) {
            return true
        }
        val b0 = addr[0]
        val b1 = addr[1]
        // 10.x.x.x/8
        val section1: Byte = 0x0A
        // 172.16.x.x/12
        val section2 = 0xAC.toByte()
        val section3 = 0x10.toByte()
        val section4 = 0x1F.toByte()
        // 192.168.x.x/16
        val section5 = 0xC0.toByte()
        val section6 = 0xA8.toByte()
        return when (b0) {
            section1 -> true
            section2 -> {
                if (b1 in section3..section4) {
                    return true
                }
                when (b1) {
                    section6 -> return true
                }
                false
            }

            section5 -> {
                when (b1) {
                    section6 -> return true
                }
                false
            }

            else -> false
        }
    }

    fun getUserAgent(): String? {
        return getRequest()?.let { getHeader(it, "User-Agent") }
    }
}
