package cn.chriswood.anthill.infrastructure.doc

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(SwaggerProperties::class)
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = "anthill.swagger",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SwaggerConfig(
    private val swaggerProperties: SwaggerProperties
) {
    @Bean
    @ConditionalOnMissingBean(OpenAPI::class)
    fun openApi(): OpenAPI {
        val openApi = OpenAPI()
        // 文档基本信息
        val infoProperties: SwaggerProperties.InfoProperties? = swaggerProperties.info
        val info: Info = convertInfo(infoProperties)
        openApi.info(info)
        // 扩展文档信息
        openApi.externalDocs(swaggerProperties.externalDocs)
        openApi.tags(swaggerProperties.tags)
        openApi.paths(swaggerProperties.paths)
        val components: Components? = swaggerProperties.components
        openApi.components(components)
        val list: MutableList<SecurityRequirement> = ArrayList()
        list.add(SecurityRequirement().addList("apikey"))
        openApi.security(list)
        return openApi
    }

    private fun convertInfo(infoProperties: SwaggerProperties.InfoProperties?): Info {
        val info = Info()
        info.title = infoProperties?.title
        info.description = infoProperties?.description
        info.contact = infoProperties?.contact
        info.license = infoProperties?.license
        info.version = infoProperties?.version
        return info
    }
}
