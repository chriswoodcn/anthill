package cn.chriswood.anthill.infrastructure.web.support

import cn.chriswood.anthill.infrastructure.core.utils.ObjectUtil
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import cn.hutool.core.io.IoUtil
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor

class WebInvokeTimeInterceptor : WebRequestInterceptor {

    private val log = LoggerFactory.getLogger(WebInvokeTimeInterceptor::class.java)
    override fun preHandle(request: WebRequest) {
        if (request is ServletWebRequest) {
            val isNeedFilterRequest = getIsNeedFilterRequest(request)
            if (isNeedFilterRequest) return
            val r0 = request.request
            val isRepeatedlyRequestWrapper = r0 is RepeatedlyRequestWrapper
            log.trace("isRepeatedlyRequestWrapper {}", isRepeatedlyRequestWrapper)
            r0.setAttribute("anthill_web_invoke_start_time", System.currentTimeMillis())
            var jsonParam: Any? = null
            if (isRepeatedlyRequestWrapper) {
                log.trace("RepeatedlyRequestWrapper {}", r0)
                jsonParam =
                    if (!r0.contentType.isNullOrEmpty() && r0.contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                        IoUtil.read(r0.reader)
                    } else {
                        if (r0.parameterMap.isNotEmpty()) {
                            JacksonUtil.bean2string(r0.parameterMap)
                        } else {
                            null
                        }
                    }
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

    /**
     * 判断是否是需要过滤的请求
     *
     * HEAD请求、swagger相关请求
     */
    private fun getIsNeedFilterRequest(request: ServletWebRequest): Boolean {
        if (request.httpMethod == HttpMethod.HEAD) return true
        val requestURI = request.request.requestURI
        val filters = arrayOf(
            "/swagger-ui",
            "/v3/api-docs"
        )
        return filters.any {
            requestURI.startsWith(it)
        }
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        if (request is ServletWebRequest) {
            val isNeedFilterRequest = getIsNeedFilterRequest(request)
            if (isNeedFilterRequest) return
            val r0 = request.request
            val startTime = r0.getAttribute("anthill_web_invoke_start_time") as Long
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
