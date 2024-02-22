package cn.chriswood.anthill.infrastructure.web.support

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType

class RepeatableFilter : Filter {
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        var requestWrapper: RepeatedlyRequestWrapper? = null
        if (p0 is HttpServletRequest
            && !p0.contentType.isNullOrEmpty()
            && p0.contentType.startsWith(MediaType.APPLICATION_JSON_VALUE, true)
        ) {
            requestWrapper = RepeatedlyRequestWrapper(p0)
        }
        p2?.doFilter(requestWrapper ?: p0, p1)
    }
}
