package cn.chriswood.anthill.infrastructure.web.interceptor

import cn.chriswood.anthill.infrastructure.json.JacksonUtil
import cn.chriswood.anthill.infrastructure.core.utils.ObjectUtil
import cn.hutool.core.io.IoUtil
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor

class WebInvokeTimeInterceptor : WebRequestInterceptor {

    private val log = LoggerFactory.getLogger(WebInvokeTimeInterceptor::class.java)
    override fun preHandle(request: WebRequest) {
        if (request is ServletWebRequest) {
            val r0 = request.request
            if (r0 !is RepeatedlyRequestWrapper) {
                return
            }
            r0.setAttribute("startTime", System.currentTimeMillis())
            val jsonParam: Any?
            jsonParam = if (!r0.contentType.isNullOrEmpty() && r0.contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                IoUtil.read(r0.reader)
            } else {
                JacksonUtil.bean2string(r0.parameterMap)
            }
            if (ObjectUtil.isNull(jsonParam)) {
                log.info(
                    "开始请求 => [{}] URL[{}]",
                    request.httpMethod,
                    r0.requestURI,
                )
            } else {
                log.info(
                    "开始请求 => [{}] URL[{}] 参数[{}]",
                    request.httpMethod,
                    r0.requestURI,
                    jsonParam
                )
            }

        }
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {

    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        if (request is ServletWebRequest) {
            val r0 = request.request
            if (r0 !is RepeatedlyRequestWrapper) {
                return
            }
            val startTime = r0.getAttribute("startTime") as Long
            val endTime = System.currentTimeMillis()
            log.info(
                "结束请求 => [{}] URL[{}],耗时[{}ms]",
                request.httpMethod,
                r0.requestURI,
                endTime - startTime
            )
        }
    }
}
