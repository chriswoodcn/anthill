package cn.chriswood.anthill.infrastructure.web.utils

import java.util.*

interface HttpRequestUtilInterface {
    fun getLang(): String
    fun getLocale(): Locale
    fun getDevice(): String
    fun getEndpoint(): String
}
