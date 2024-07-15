package cn.chriswood.anthill.infrastructure.web.annotation.log

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Log(
    val module: String = "SYSTEM",
    val operate: OperateType = OperateType.OTHER,
    val user: UserType = UserType.UNKNOWN,
    val appender: KClass<out LogAppender>,
    val message: String = ""
)
