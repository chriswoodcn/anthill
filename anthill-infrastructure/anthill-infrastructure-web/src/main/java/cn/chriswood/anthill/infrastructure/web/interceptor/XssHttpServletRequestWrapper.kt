package cn.chriswood.anthill.infrastructure.web.interceptor

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.http.HtmlUtil
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.MediaType
import java.io.IOException
import java.nio.charset.StandardCharsets

class XssHttpServletRequestWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {
    override fun getParameterValues(name: String?): Array<String?> {
        val values = super.getParameterValues(name)
        if (values != null) {
            val length = values.size
            val escapeValues = arrayOfNulls<String>(length)
            for (i in 0..<length) {
                // 防xss攻击和过滤前后空格
                escapeValues[i] = HtmlUtil.cleanHtmlTag(values[i]).trim()
            }
            return escapeValues
        }
        return super.getParameterValues(name)
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream()
        }

        // 为空，直接返回
        var json = StrUtil.str(IoUtil.readBytes(super.getInputStream(), false), StandardCharsets.UTF_8)
        if (StringUtil.isEmpty(json)) {
            return super.getInputStream()
        }

        // xss过滤
        json = HtmlUtil.cleanHtmlTag(json).trim { it <= ' ' }
        val jsonBytes = json.toByteArray(StandardCharsets.UTF_8)
        val bis = IoUtil.toStream(jsonBytes)
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return true
            }

            override fun isReady(): Boolean {
                return true
            }

            @Throws(IOException::class)
            override fun available(): Int {
                return jsonBytes.size
            }

            override fun setReadListener(readListener: ReadListener) {}

            @Throws(IOException::class)
            override fun read(): Int {
                return bis.read()
            }
        }
    }
    private fun isJsonRequest(): Boolean {
        val header = super.getContentType()
        return header.startsWith(MediaType.APPLICATION_JSON_VALUE, true)
    }
}
