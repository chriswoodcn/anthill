package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.support.Constants
import java.lang.annotation.Inherited

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Inherited
annotation class DDS(val value: String = Constants.PRIMARY)
