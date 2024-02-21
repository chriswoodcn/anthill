package cn.chriswood.anthill.infrastructure.web.utils

import cn.chriswood.anthill.infrastructure.web.exception.InfrastructureWebEnum
import cn.hutool.extra.spring.SpringUtil
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*

object I18nMessageUtil {
    fun message(key: String, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(key, args, HttpRequestUtil.getLocale())
        } catch (e: NoSuchMessageException) {
            null
        } catch (e: Exception) {
            InfrastructureWebEnum.FUNC_ERROR.eject("I18nMessageUtil", "message", e.message)
            null
        }
    }

    fun messageByLang(code: Int, lang: String, vararg args: Any): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(code.toString(), args, Locale(lang))
        } catch (e: NoSuchMessageException) {
            null
        } catch (e: Exception) {
            InfrastructureWebEnum.FUNC_ERROR.eject("I18nMessageUtil", "messageByLang", e.message)
            null
        }
    }
}

