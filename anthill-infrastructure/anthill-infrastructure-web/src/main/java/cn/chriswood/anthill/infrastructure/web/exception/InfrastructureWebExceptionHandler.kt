package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.constants.HttpStatus
import cn.chriswood.anthill.infrastructure.core.exception.BaseException
import cn.chriswood.anthill.infrastructure.core.exception.InfrastructureException
import cn.chriswood.anthill.infrastructure.web.base.R
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@ConditionalOnWebApplication
@RestControllerAdvice
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
class InfrastructureWebExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init InfrastructureWebExceptionHandler >>>>>>>>>>")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("Exception >>> RequestURI[{}], MESSAGE[{}]", requestURI, e.message)
        if (e !is NoResourceFoundException) {
            e.printStackTrace()
        }
        return R.fail(HttpStatus.NOT_FOUND, e.message)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("BaseException >>> Module[{}], RequestURI[{}], MESSAGE[{}]", e.module, requestURI, e.message)
        return R.fail(HttpStatus.FAIL, e.message)
    }

    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureWebException(e: InfrastructureException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error(
            "InfrastructureException >>> Module[{}], RequestURI[{}], MESSAGE[{}]",
            e.module,
            requestURI,
            e.message
        )
        return R.fail(HttpStatus.FAIL, e.message)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("BindException >>> RequestURI[{}], MESSAGE[{}]", requestURI, e.message)
        log.error("${e.bindingResult.fieldErrors}")
        val message = e.bindingResult.fieldErrors.map {
            "${it.field} ${it.defaultMessage ?: "invalid"}"
        }
        return R.fail(message.joinToString(";"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): R<Void> {
        val requestURI = request.requestURI
        log.error("MethodArgumentNotValidException >>> RequestURI[{}], MESSAGE[{}]", requestURI, e.message)
        log.error("${e.bindingResult.fieldErrors}")
        val message = e.bindingResult.fieldErrors.map {
            "${it.field} ${it.defaultMessage ?: "invalid"}"
        }
        return R.fail(message.joinToString(";"))
    }
}
