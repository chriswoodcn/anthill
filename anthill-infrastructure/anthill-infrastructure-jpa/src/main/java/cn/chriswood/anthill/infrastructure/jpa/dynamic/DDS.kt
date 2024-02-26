package cn.chriswood.anthill.infrastructure.jpa.dynamic

import cn.chriswood.anthill.infrastructure.jpa.support.Constants
import java.lang.annotation.Inherited

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Inherited
annotation class DDS(val value: String = Constants.PRIMARY)
