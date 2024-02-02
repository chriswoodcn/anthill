package cn.chriswood.anthill.infrastructure.datasource.common

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata


class DataSourceValidCondition : Condition {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val environment = context.environment
        val binder = Binder.get(environment)
        val map = binder.bindOrCreate(
            "anthill.jpa.multi",
            Bindable.mapOf(String::class.java, JpaDataSourceProperty::class.java)
        )
        var res = true
        map.forEach {
            val validateWithPackage = it.value.validateWithPackage()
            if (!validateWithPackage) {
                res = false
                log.error("数据源校验出错，数据源名称{}", it.key)
                return@forEach
            }
        }

        return res
    }
}
