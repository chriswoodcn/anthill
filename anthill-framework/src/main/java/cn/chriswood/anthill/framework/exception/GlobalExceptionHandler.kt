package cn.chriswood.anthill.framework.exception

import cn.chriswood.anthill.framework.web.R
import cn.chriswood.anthill.infrastructure.core.constants.HttpStatus
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import org.slf4j.LoggerFactory
import jakarta.servlet.http.HttpServletRequest
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("请求地址'{}',错误'{}'", requestURI, e.message)
        return R.fail(HttpStatus.NOT_FOUND, e.message)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("请求地址'{}',错误'{}'", requestURI, e.message)
        return R.fail(HttpStatus.FAIL, e.message)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): R<Void> {
        log.error(e.message)
        val collect = e.allErrors.stream().map { it.defaultMessage!! }.collect(Collectors.toList())
        val message: String = StringUtil.join(collect, ',')
        return R.fail(message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): R<Void> {
        log.error(e.message)
        val message = e.bindingResult.fieldError!!.defaultMessage!!
        return R.fail(message)
    }
}
