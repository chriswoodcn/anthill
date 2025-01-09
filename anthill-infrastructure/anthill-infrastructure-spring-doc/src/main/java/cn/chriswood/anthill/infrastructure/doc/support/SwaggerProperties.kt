package cn.chriswood.anthill.infrastructure.doc.support

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.tags.Tag
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("anthill.swagger")
data class SwaggerProperties(
    /**
     * 开关
     */
    val enabled: Boolean?,

    /**
     * 是否需要支持Validation Group分组生成接口的schema
     */
    val enableValidationGroup: Boolean? = false,

    /**
     * 标题
     */
    val title: String? = null,

    /**
     * 描述
     */
    val description: String? = null,

    /**
     * 版本
     */
    val version: String? = null,

    /**
     * 联系人信息
     */
    @NestedConfigurationProperty
    val contact: Contact? = null,

    /**
     * 许可证
     */
    @NestedConfigurationProperty
    val license: License? = null,


    /**
     * 扩展文档地址
     */
    val externalDocs: ExternalDocumentation? = null,

    /**
     * 标签
     */
    val tags: List<Tag>? = null,

    /**
     * 路径
     */
    val paths: Paths? = null,

    /**
     * 组件
     */
    val components: Components? = null,
)
