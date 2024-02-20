package cn.chriswood.anthill.infrastructure.doc

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
     * 文档基本信息
     */
    @NestedConfigurationProperty
    var info: InfoProperties? = null,

    /**
     * 扩展文档地址
     */
    @NestedConfigurationProperty
    val externalDocs: ExternalDocumentation? = null,

    /**
     * 标签
     */
    val tags: List<Tag>? = null,

    /**
     * 路径
     */
    @NestedConfigurationProperty
    val paths: Paths? = null,

    /**
     * 组件
     */
    @NestedConfigurationProperty
    val components: Components? = null,
) {
    data class InfoProperties(
        /**
         * 标题
         */
        val title: String? = null,

        /**
         * 描述
         */
        val description: String? = null,

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
         * 版本
         */
        val version: String? = null,
    )
}
