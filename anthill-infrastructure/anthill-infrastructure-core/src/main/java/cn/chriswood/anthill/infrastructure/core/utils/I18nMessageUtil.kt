package cn.chriswood.anthill.infrastructure.core.utils

import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

object I18nMessageUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    fun message(key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(key, args, LocaleContextHolder.getLocale())
        } catch (e: Exception) {
            log.error("I18nMessageUtil message invoke error: {}", e.message)
            null
        }
    }

    fun messageByLocale(locale: Locale, key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(key, args, locale)
        } catch (e: Exception) {
            log.error("I18nMessageUtil messageByLocale invoke error: {}", e.message)
            null
        }
    }

    fun messageByLang(lang: String, key: String, vararg args: Any): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(key, args, Locale(lang))
        } catch (e: Exception) {
            log.error("I18nMessageUtil messageByLang invoke error: {}", e.message)
            null
        }
    }
}

