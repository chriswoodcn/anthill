package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.constants.HttpStatus
import cn.chriswood.anthill.infrastructure.core.exception.BaseException
import cn.chriswood.anthill.infrastructure.core.exception.InfrastructureException
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
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
        return R.fail(HttpStatus.FAIL, e.message)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("BaseException >>> Module[{}], RequestURI[{}], MESSAGE[{}]", e.module, requestURI, e.message)
        return R.fail(e.code, e.message)
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
        return R.fail(e.code, e.message)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("BindException >>> RequestURI[{}], MESSAGE[{}]", requestURI, e.bindingResult.allErrors)
        val allErrors = e.bindingResult.allErrors
        log.error("$allErrors")
        val validationMessages = genValidationMessages(allErrors)
        return R.fail(HttpStatus.BAD_REQUEST, validationMessages)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): R<Void> {
        val requestURI = request.requestURI
        log.error(
            "MethodArgumentNotValidException >>> RequestURI[{}], MESSAGE[{}]",
            requestURI,
            e.bindingResult.allErrors
        )
        val allErrors = e.bindingResult.allErrors
        log.error("$allErrors")
        val validationMessages = genValidationMessages(allErrors)
        return R.fail(HttpStatus.BAD_REQUEST, validationMessages)
    }

    private fun genValidationMessages(allErrors: List<ObjectError>): String {
        val lang = HttpRequestUtil.getLang()
        val message = allErrors.map {
            if (it is FieldError) {
                log.trace(
                    """
                genValidationMessages
                field: ${it.field}
                defaultMessage: ${it.defaultMessage}
                code: ${it.code}
                codes: ${it.codes}
                codes[0]: ${it.codes?.get(0)}
                 """.trimIndent()
                )
                "${it.field} ${it.defaultMessage ?: "invalid"}"
            } else {
                log.trace(
                    """
                genValidationMessages
                defaultMessage: ${it.defaultMessage}
                code: ${it.code}
                codes: ${it.codes}
                codes[0]: ${it.codes?.get(0)}
                 """.trimIndent()
                )

                it.defaultMessage ?: "${it.objectName} invalid"
            }

        }
        return message.joinToString(";")
    }
}
