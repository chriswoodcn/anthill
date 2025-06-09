package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.config.ExceptionConfig
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.text.MessageFormat
import java.util.*

object I18nMessageUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    private const val DEFAULT_LANGUAGE = "en"

    fun message(key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            log.debug("key {}", key)
            messageSource.getMessage(key, args, LocaleContextHolder.getLocale())
        } catch (e: Exception) {
            log.error("I18nMessageUtil message invoke error: {}", e.message)
            null
        }
    }

    fun messageByLocale(locale: Locale, key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            log.debug("locale {} key {}", locale, key)
            messageSource.getMessage(key, args, locale)
        } catch (e: Exception) {
            log.error("I18nMessageUtil messageByLocale invoke error: {}", e.message)
            null
        }
    }

    fun messageByLang(lang: String, key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            log.debug("lang {} key {}", lang, key)
            messageSource.getMessage(key, args, Locale(lang))
        } catch (e: Exception) {
            log.error("I18nMessageUtil messageByLang invoke error: {}", e.message)
            null
        }
    }

    fun innerMessageByLang(
        module: String,
        map: Map<String, Map<String, String>>,
        lang: String,
        key: String,
        vararg args: Any?
    ): String? {
        val config: ExceptionConfig = SpringUtil.getBean(ExceptionConfig::class.java)
        if (config.enableCustomMessage && config.customMessageModule.contains(module)) {
            return messageByLang(lang, key, *args)
        }
        val stringMap = map[lang] ?: map[DEFAULT_LANGUAGE]!!
        log.debug("stringMap {}", stringMap)
        val stringFormat = stringMap[key] ?: return null
        log.debug("stringFormat {}", stringFormat)
        return MessageFormat.format(stringFormat, *args)
    }
}

