package cn.chriswood.anthill.infrastructure.core.exception

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil

open class InfrastructureException(
    override var code: Int,
    override var module: String = DEFAULT_MODULE,
    override var dialect: String,
    override var args: Array<out Any?> = emptyArray()
) : BaseException(code, module, dialect, args) {
    private var customMessage: Boolean = false
    override var message: String = BaseException.DEFAULT_MESSAGE
        set(value) {
            customMessage = true
            field = value
        }
        get() {
            if (customMessage) return field
            val i18nMessage = I18nMessageUtil.message(
                "${module}.$dialect", *args
            )
            return if (!i18nMessage.isNullOrEmpty()) {
                i18nMessage
            } else {
                DEFAULT_MESSAGE
            }
        }

    companion object {
        const val DEFAULT_MESSAGE = "InfrastructureException error"
        const val DEFAULT_MODULE = "Infrastructure"
    }

    constructor(code: Int, dialect: String, vararg args: Any?) :
        this(code, DEFAULT_MODULE, dialect, args)
}
