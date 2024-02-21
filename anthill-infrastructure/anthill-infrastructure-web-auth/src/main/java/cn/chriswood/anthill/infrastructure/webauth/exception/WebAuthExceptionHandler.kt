package cn.chriswood.anthill.infrastructure.webauth.exception

import cn.chriswood.anthill.infrastructure.core.constants.HttpStatus
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.exception.SaTokenException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@ConditionalOnWebApplication
@RestControllerAdvice
@Order(0)
@AutoConfiguration
class WebAuthExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init WebAuthExceptionHandler >>>>>>>>>>")
    }

    @ExceptionHandler(SaTokenException::class)
    fun handleSaTokenException(e: SaTokenException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("请求地址'{}',错误'{}'", requestURI, e.message)
        return R.fail(HttpStatus.UNAUTHORIZED, e.message)
    }
}
