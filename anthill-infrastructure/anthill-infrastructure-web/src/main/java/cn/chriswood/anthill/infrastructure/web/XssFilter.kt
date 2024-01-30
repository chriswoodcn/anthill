package cn.chriswood.anthill.infrastructure.web

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod

class XssFilter : Filter {

    private var excludes: List<String> = ArrayList()

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        val tempExcludes = filterConfig.getInitParameter("excludes")
        if (StringUtil.isNotEmpty(tempExcludes)) {
            val url: Array<String> = tempExcludes.split(StringUtil.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var i = 0
            while (i < url.size) {
                excludes.plus(url[i])
                i++
            }
        }
    }

    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        val req = p0 as HttpServletRequest
        val resp = p1 as HttpServletResponse
        if (handleExcludeURL(req, resp)) {
            p2?.doFilter(p0, p1)
            return
        }
        val xssRequest = XssHttpServletRequestWrapper(p0)
        p2?.doFilter(xssRequest, p1)
    }

    private fun handleExcludeURL(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        val url = request.servletPath
        val method = request.method
        // GET DELETE 不过滤
        return if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            true
        } else {
            return StringUtil.matches(url, excludes)
        }
    }
}
