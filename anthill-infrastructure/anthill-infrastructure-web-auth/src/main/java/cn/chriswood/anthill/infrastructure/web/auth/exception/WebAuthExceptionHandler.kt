package cn.chriswood.anthill.infrastructure.web.auth.exception

import cn.chriswood.anthill.infrastructure.core.constants.HttpStatus
import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import cn.dev33.satoken.exception.SaTokenException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@ConditionalOnWebApplication
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
class WebAuthExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    private val messageDialectMap = hashMapOf(
        11011 to "InfrastructureWebAuth.noToken",
        11012 to "InfrastructureWebAuth.invalidToken",
        11013 to "InfrastructureWebAuth.expireToken",
        11014 to "InfrastructureWebAuth.pushOffline",
        11015 to "InfrastructureWebAuth.kickOffline",
        11016 to "InfrastructureWebAuth.frozen",
        11041 to "InfrastructureWebAuth.noCorrespondRole",
        11051 to "InfrastructureWebAuth.noCorrespondPermission",
    )

    init {
        log.debug(">>>>>>>>>> init WebAuthExceptionHandler >>>>>>>>>>")
    }

    @ExceptionHandler(SaTokenException::class)
    fun handleSaTokenException(e: SaTokenException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error("SaTokenException >>> RequestURI[{}], MESSAGE[{}]", requestURI, e.message)
        var dialectName = "InfrastructureWebAuth.message"
        if (e.code in messageDialectMap.keys) {
            dialectName = messageDialectMap[e.code]!!
        }
        return R.fail(
            HttpStatus.UNAUTHORIZED,
            I18nMessageUtil.messageByLang(HttpRequestUtil.getLang(), dialectName)
        )
    }
}
