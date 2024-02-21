package cn.chriswood.anthill.infrastructure.core.exception

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@ConditionalOnNotWebApplication
@RestControllerAdvice
@AutoConfiguration
@Order(Ordered.HIGHEST_PRECEDENCE)
class InfrastructureExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init InfrastructureExceptionHandler >>>>>>>>>>")
    }

    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(e: InfrastructureException) {
        log.error(
            "InfrastructureException >>> Module[{}], MESSAGE[{}]",
            e.module,
            e.message
        )
    }
}
