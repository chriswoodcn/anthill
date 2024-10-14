package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.annotation.handler.SaAnnotationHandlerInterface
import cn.dev33.satoken.annotation.handler.SaCheckLoginHandler

import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class SysUserCheckLogin

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class AppUserCheckLogin

@Component
class SysUserCheckLoginHandler : SaAnnotationHandlerInterface<SysUserCheckLogin> {
    override fun getHandlerAnnotationClass(): Class<SysUserCheckLogin> {
        return SysUserCheckLogin::class.java
    }

    override fun checkMethod(at: SysUserCheckLogin?, method: Method?) {
        SaCheckLoginHandler._checkMethod(UserType.SYS_USER.code)
    }
}

@Component
class AppUserCheckLoginHandler : SaAnnotationHandlerInterface<AppUserCheckLogin> {
    override fun getHandlerAnnotationClass(): Class<AppUserCheckLogin> {
        return AppUserCheckLogin::class.java
    }

    override fun checkMethod(at: AppUserCheckLogin?, method: Method?) {
        SaCheckLoginHandler._checkMethod(UserType.APP_USER.code)
    }
}
