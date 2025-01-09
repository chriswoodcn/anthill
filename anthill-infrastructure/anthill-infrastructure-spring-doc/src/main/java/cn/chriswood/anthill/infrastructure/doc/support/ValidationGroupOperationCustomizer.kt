package cn.chriswood.anthill.infrastructure.doc.support

import cn.chriswood.anthill.infrastructure.core.logger
import cn.hutool.json.JSONUtil
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.method.HandlerMethod

data class SchemaConstraints(
    var originSchemaName: String,
    var newSchemaName: String,
    var required: List<String>
)

class CustomSchemaConstraints(val operationId: String) {
    var schemas: MutableList<SchemaConstraints> = mutableListOf()
}

class ValidationGroupOperationCustomizer : OperationCustomizer {

    companion object {
        const val CUSTOM_SCHEMA_CONSTRAINTS = "CustomSchemaConstraints"
    }

    private val log = logger<ValidationGroupOperationCustomizer>()
    override fun customize(operation: Operation, handlerMethod: HandlerMethod): Operation {
        val parameters = handlerMethod.methodParameters
        val customSchemaConstraints = CustomSchemaConstraints(operation.operationId)
        parameters.forEach { parameter ->
            log.info(JSONUtil.toJsonPrettyStr(parameter))
            log.info(parameter.executable.name)
            log.info(parameter.parameterType.name)
            //首先必需是自定义类型才需要修改schema
            if (isCustomType(parameter.parameterType)) {
                //需要有Validated注解
                if (parameter.hasParameterAnnotation(Validated::class.java)) {
                    log.info("hasParameterAnnotation Validated")
                    val validated = parameter.getParameterAnnotation(Validated::class.java)
                    //拿到该接口 该参数的分组信息
                    val groups = validated?.value?.map { it.java.simpleName } ?: emptyList()
                    //分组信息为空 则表示没有分组 原有schema改成EmptyGroup结尾
                    if (groups.isEmpty()) {
                        replaceNewSchema(
                            operation,
                            parameter,
                            parameter.parameterType.simpleName,
                            "${parameter.parameterType.simpleName}EmptyGroup"
                        )
                        //TODO  customSchemaConstraints只需要添加没有分组信息的必需字段
                        customSchemaConstraints.schemas.add(
                            SchemaConstraints(
                                parameter.parameterType.simpleName,
                                "${parameter.parameterType.simpleName}EmptyGroup",
                                getRequiredFields(parameter, groups)
                            )
                        )
                    } else {
                        replaceNewSchema(
                            operation,
                            parameter,
                            parameter.parameterType.simpleName,
                            "${parameter.parameterType.simpleName}${groups.joinToString(separator = "")}"
                        )
                        //TODO customSchemaConstraints需要添加有的分组信息的必需字段
                        customSchemaConstraints.schemas.add(
                            SchemaConstraints(
                                parameter.parameterType.simpleName,
                                "${parameter.parameterType.simpleName}${groups.joinToString(separator = "")}",
                                getRequiredFields(parameter, groups)
                            )
                        )
                    }
                }

            }
        }
        if (operation.extensions == null) {
            operation.extensions = mutableMapOf()
        }
        //customSchemaConstraints附加到operation上
        operation.extensions[CUSTOM_SCHEMA_CONSTRAINTS] = customSchemaConstraints
        return operation
    }

    private fun isCustomType(javaClass: Class<*>): Boolean {
        return when {
            // 检查是否是Java的基本类型或包装类型
            javaClass.isPrimitive -> false
            javaClass == String::class.java -> false
            Number::class.java.isAssignableFrom(javaClass) -> false
            javaClass == Boolean::class.java -> false
            javaClass == Char::class.java -> false
            javaClass == java.lang.Character::class.java -> false
            javaClass == java.lang.Boolean::class.java -> false
            javaClass == java.lang.Number::class.java -> false
            javaClass == java.lang.String::class.java -> false
            javaClass == java.lang.Void::class.java -> false
            javaClass == java.lang.Object::class.java -> false
            javaClass == Any::class.java -> false
            javaClass == Nothing::class.java -> false
            javaClass == Unit::class.java -> false
            // 检查是否是Java的核心类（如集合类等）
            javaClass.name.startsWith("java.") -> false
            javaClass.name.startsWith("javax.") -> false
            // 检查是否是Kotlin的标准库类
            javaClass.name.startsWith("kotlin.") -> false
            // 检查是否是枚举类型或注解类型
            javaClass.isEnum -> false
            javaClass.isAnnotation -> false
            else -> true
        }
    }

    private fun getOriginSchema(
        operation: Operation,
        parameter: MethodParameter,
        originSchemaName: String
    ): Schema<*>? {
        return if (parameter.hasParameterAnnotation(RequestBody::class.java)) {
            //带有RequestBody注解 schema在requestBody中
            operation.requestBody.content[MediaType.APPLICATION_JSON_VALUE]
                ?.schema
        } else {
            //否则 schema在parameters中
            operation.parameters.find {
                it.schema.`$ref`
                    .endsWith("/${originSchemaName}")
            }?.schema
        }
    }

    private fun replaceNewSchema(
        operation: Operation,
        parameter: MethodParameter,
        originSchemaName: String,
        newSchemaName: String
    ) {
        val originSchema = getOriginSchema(operation, parameter, originSchemaName) ?: return
        originSchema.`$ref` = newSchemaName
    }

    /**
     * @param parameter 接口方法参数
     * @param groups Validated注解上的groups
     */
    private fun getRequiredFields(parameter: MethodParameter, groups: List<String>): List<String> {
        val requiredFields = mutableListOf<String>()
        parameter.parameterType.declaredFields.forEach { f ->
            f.declaredAnnotations.forEach {
                when (it) {
                    is NotNull -> {
                        if (groups.isEmpty()) requiredFields.add(f.name)
                        //方法参数对象上当前field上的groups
                        val fGroups = it.groups.map { g -> g.java.simpleName }
                        //当前field上的groups为空 ，则这个字段是必填项目
                        if (fGroups.isEmpty()) requiredFields.add(f.name)
                        //Validated注解上的groups包含在当前field上的groups，则这个字段是必填项目
                        if (groups.any { g -> fGroups.contains(g) }) {
                            requiredFields.add(f.name)
                        }
                    }

                    is NotBlank -> {
                        if (groups.isEmpty()) requiredFields.add(f.name)
                        val fGroups = it.groups.map { g -> g.java.simpleName }
                        if (fGroups.isEmpty()) requiredFields.add(f.name)
                        if (groups.any { g -> fGroups.contains(g) }) {
                            requiredFields.add(f.name)
                        }
                    }
                }
            }
        }
        return requiredFields
    }
}
