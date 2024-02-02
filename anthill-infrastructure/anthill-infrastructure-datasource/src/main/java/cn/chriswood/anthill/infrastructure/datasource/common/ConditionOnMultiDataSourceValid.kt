package cn.chriswood.anthill.infrastructure.datasource.common

import org.springframework.context.annotation.Conditional

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Conditional(DataSourceValidCondition::class)
annotation class ConditionOnMultiDataSourceValid
