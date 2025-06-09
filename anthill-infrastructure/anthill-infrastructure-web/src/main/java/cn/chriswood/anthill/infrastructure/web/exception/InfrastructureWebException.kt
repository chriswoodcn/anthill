package cn.chriswood.anthill.infrastructure.web.exception

import cn.chriswood.anthill.infrastructure.core.exception.BaseException
import cn.chriswood.anthill.infrastructure.core.exception.InfrastructureException
import cn.chriswood.anthill.infrastructure.core.logger
import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil
import org.springframework.context.i18n.LocaleContextHolder

class InfrastructureWebException(
    override var code: Int,
    override var module: String = DEFAULT_MODULE,
    override var dialect: String,
    override var args: Array<out Any?> = emptyArray()
) : InfrastructureException(code, module, dialect, args) {

    private val log = logger()

    private var customMessage: Boolean = false
    override var message: String = BaseException.DEFAULT_MESSAGE
        set(value) {
            customMessage = true
            field = value
        }
        get() {
            if (customMessage) return field
            log.debug(
                "module {} dialect {} language {}",
                DEFAULT_MODULE,
                dialect,
                LocaleContextHolder.getLocale().language
            )
            val innerI18nMessage = I18nMessageUtil.innerMessageByLang(
                DEFAULT_MODULE,
                InfrastructureWebExceptionMessages.messages,
                LocaleContextHolder.getLocale().language,
                dialect,
                *args
            )
            log.debug("innerI18nMessage {}", innerI18nMessage)
            if (!innerI18nMessage.isNullOrEmpty()) {
                return innerI18nMessage
            } else {
                val i18nMessage = I18nMessageUtil.messageByLang(
                    LocaleContextHolder.getLocale().language,
                    dialect,
                    *args
                )
                log.debug("i18nMessage {}", innerI18nMessage)
                return i18nMessage ?: DEFAULT_MESSAGE
            }
        }

    companion object {
        const val DEFAULT_MESSAGE = "InfrastructureWebException error"
        const val DEFAULT_MODULE = "InfrastructureWeb"
    }

    constructor(code: Int, dialect: String, vararg args: Any?) :
        this(code, DEFAULT_MODULE, dialect, args)
}

