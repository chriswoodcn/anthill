package cn.chriswood.anthill.datasource

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
data class DataSourceProperties(
    @NestedConfigurationProperty
    val dataSources: Map<String, DataSourceProperty?>
) {
    companion object {
        const val PREFIX = "spring.datasource.multi"
        const val POOL_PREFIX = "HikariPool-"
    }

    data class DataSourceProperty(
        val driver: String,
        val url: String,
        val username: String,
        val password: String,
        val query: String,
        val primary: Boolean,
        val packageScan: String
    )
}
