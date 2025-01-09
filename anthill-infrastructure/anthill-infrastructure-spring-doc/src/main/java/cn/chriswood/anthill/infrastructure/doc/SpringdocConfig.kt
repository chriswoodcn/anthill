package cn.chriswood.anthill.infrastructure.doc

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.chriswood.anthill.infrastructure.doc.support.CustomSchemaConstraints
import cn.chriswood.anthill.infrastructure.doc.support.OpenAPIServiceCustomizer
import cn.chriswood.anthill.infrastructure.doc.support.SwaggerProperties
import cn.chriswood.anthill.infrastructure.doc.support.ValidationGroupOperationCustomizer
import cn.hutool.core.bean.BeanUtil
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springdoc.core.customizers.OpenApiBuilderCustomizer
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.customizers.ServerBaseUrlCustomizer
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.JavadocProvider
import org.springdoc.core.service.OpenAPIService
import org.springdoc.core.service.SecurityService
import org.springdoc.core.utils.PropertyResolverUtils
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import java.util.*

@AutoConfiguration
@EnableConfigurationProperties(SwaggerProperties::class)
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = "anthill.swagger",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SpringdocConfig(
    private val swaggerProperties: SwaggerProperties,
    private val serverProperties: ServerProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(OpenAPI::class)
    fun openApi(): OpenAPI {
        val openApi = OpenAPI()
        // 文档基本信息
        val info: Info = convertInfo(swaggerProperties)
        openApi.info(info)
        // 扩展文档信息
        openApi.externalDocs(swaggerProperties.externalDocs)
        openApi.tags(swaggerProperties.tags)
        openApi.paths(swaggerProperties.paths)
        val components: Components? = swaggerProperties.components
        openApi.components(components)
        val keySet: MutableSet<String>? = swaggerProperties.components?.securitySchemes?.keys
        if (!keySet.isNullOrEmpty()) {
            val list: MutableList<SecurityRequirement> = ArrayList()
            val securityRequirement = SecurityRequirement()
            keySet.forEach {
                securityRequirement.addList(it)
            }
            list.add(securityRequirement)
            openApi.security(list)
        }
        log.debug(">>>>>>>>>> init SwaggerConfig openApi >>>>>>>>>>")
        return openApi
    }

    private fun convertInfo(infoProperties: SwaggerProperties?): Info {
        val info = Info()
        info.title = infoProperties?.title
        info.description = infoProperties?.description
        info.contact = infoProperties?.contact
        info.license = infoProperties?.license
        info.version = infoProperties?.version
        return info
    }


    /**
     * 自定义 openapi 处理器
     */
    @Bean
    fun openApiBuilder(
        openAPI: Optional<OpenAPI>,
        securityParser: SecurityService,
        springDocConfigProperties: SpringDocConfigProperties,
        propertyResolverUtils: PropertyResolverUtils,
        openApiBuilderCustomizers: Optional<List<OpenApiBuilderCustomizer>>,
        serverBaseUrlCustomizers: Optional<List<ServerBaseUrlCustomizer>>,
        javadocProvider: Optional<JavadocProvider>
    ): OpenAPIService {
        return OpenAPIServiceCustomizer(
            openAPI,
            securityParser,
            springDocConfigProperties,
            propertyResolverUtils,
            openApiBuilderCustomizers,
            serverBaseUrlCustomizers,
            javadocProvider
        )
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        val contextPath: String? = serverProperties.servlet.contextPath
        val finalContextPath: String = if (StringUtil.isBlank(contextPath) || "/" == contextPath) {
            ""
        } else {
            contextPath!!
        }
        // 对所有路径增加前置上下文路径
        return OpenApiCustomizer { openApi: OpenAPI ->
            val oldPaths = openApi.paths
            if (oldPaths is HackPaths) {
                return@OpenApiCustomizer
            }
            val newPaths = HackPaths()
            oldPaths.forEach { k: String, v: PathItem? ->
                if (swaggerProperties.enableValidationGroup == true) {
                    val options = v?.readOperations()
                    if (!options.isNullOrEmpty()) {
                        options.forEach { op ->
                            log.info("operationId {}", op.operationId)
                            val constraints =
                                op.extensions[ValidationGroupOperationCustomizer.CUSTOM_SCHEMA_CONSTRAINTS]
                            log.info("customSchemaConstraints {}", constraints)
                            constraints?.let { c ->
                                val customSchemaConstraints = c as CustomSchemaConstraints
                                if (op.operationId != customSchemaConstraints.operationId) return@let
                                customSchemaConstraints.schemas.forEach { s ->
                                    openApi.components.schemas[s.originSchemaName]?.let { originSchema ->
                                        val newSchema = Schema<Any>()
                                        BeanUtil.copyProperties(originSchema, newSchema)
                                        newSchema.required = s.required
                                        openApi.components.schemas[s.newSchemaName] = newSchema
                                    }
                                }
                            }
                        }
                    }
                }
                newPaths.addPathItem(
                    finalContextPath + k,
                    v
                )
            }
            openApi.paths = newPaths
        }
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "anthill.swagger",
        name = ["enable-validation-group"],
        havingValue = "true",
    )
    fun validationGroupOperationCustomizer(): OperationCustomizer {
        return ValidationGroupOperationCustomizer()
    }


    internal class HackPaths : Paths()
}
