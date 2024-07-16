package cn.chriswood.anthill.infrastructure.core.validate

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil
import cn.chriswood.anthill.infrastructure.core.utils.ObjectUtil
import cn.hutool.core.util.ReflectUtil
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
@MustBeDocumented
@Constraint(validatedBy = [RequiredOnCondition.RequiredOnConditionValidator::class])
annotation class RequiredOnCondition(
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RequiredValue(
        /**
         * js脚本语法 返回boolean值
         * example：{conditionItemName1}=‘1’&&{conditionItemName2}>10
         * conditionItemName1 conditionItemName2是条件的字段名
         */
        val conditions: String = "!1",
        val message: String = "{Validation.NotBlank}",
    )

    class RequiredOnConditionValidator : ConstraintValidator<RequiredOnCondition, Any> {
        override fun isValid(p0: Any?, p1: ConstraintValidatorContext): Boolean {
            if (null == p0) return false
            val failMessages = ArrayList<String>()
            val fields = ReflectUtil.getFields(p0::class.java)
            val collect = Arrays.stream(fields).filter { f: Field ->
                f.isAnnotationPresent(RequiredValue::class.java)
            }.collect(Collectors.toList())
            val result = collect.stream().map { field: Field ->
                val conditions: String = field.getAnnotation(RequiredValue::class.java).conditions
                val message = field.getAnnotation(RequiredValue::class.java).message
                val b = ValidationUtil.executeDynamicScript(ValidationUtil.replaceValue(conditions, p0))
                // 条件执行结果为true 需要去校验本字段
                if (b) {
                    val fieldName = field.name
                    val value = ReflectUtil.getFieldValue(p0, fieldName)
                    if (ObjectUtil.isEmpty(value)) {
                        failMessages + "$fieldName ${I18nMessageUtil.message(message)}"
                        return@map false
                    }
                    return@map true
                }
                // 条件执行结果为false 不需要去校验本字段 直接放行
                true
            }.collect(Collectors.toList())
            p1.disableDefaultConstraintViolation()
            p1.buildConstraintViolationWithTemplate(failMessages.joinToString(";")).addConstraintViolation()
            return result.stream().allMatch { it }
        }
    }
}


