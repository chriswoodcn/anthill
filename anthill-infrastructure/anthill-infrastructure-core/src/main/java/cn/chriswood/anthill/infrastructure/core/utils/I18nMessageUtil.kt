package cn.chriswood.anthill.infrastructure.core.utils

import cn.chriswood.anthill.infrastructure.core.exception.SystemExceptionEnum
import cn.hutool.extra.spring.SpringUtil
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*

object I18nMessageUtil {
    fun message(code: Int, vararg args: Any?): String? {
        val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
        return try {
            messageSource.getMessage(code.toString(), args, HttpRequestUtil.getLocale())
        } catch (e: NoSuchMessageException) {
            null
        } catch (e: Exception) {
            SystemExceptionEnum.SEVER_FUNC_ERROR.eject("MessageUtil", "message", e.message)
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
            SystemExceptionEnum.SEVER_FUNC_ERROR.eject("MessageUtil", "messageByLang", e.message)
            null
        }
    }
}

