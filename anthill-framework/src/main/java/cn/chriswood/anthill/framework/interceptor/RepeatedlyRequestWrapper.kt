package cn.chriswood.anthill.framework.interceptor

import cn.hutool.core.io.IoUtil
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class RepeatedlyRequestWrapper(
    private val request: HttpServletRequest,
) : HttpServletRequestWrapper(request) {
    val body: ByteArray

    init {
        request.characterEncoding = StandardCharsets.UTF_8.name()
        body = IoUtil.readBytes(request.inputStream, false)
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream()))
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        val basis = ByteArrayInputStream(body)
        return object : ServletInputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return basis.read()
            }

            @Throws(IOException::class)
            override fun available(): Int {
                return body.size
            }

            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {}
        }
    }

}
