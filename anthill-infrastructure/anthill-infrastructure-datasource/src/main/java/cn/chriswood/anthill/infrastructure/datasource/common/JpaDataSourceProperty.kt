package cn.chriswood.anthill.infrastructure.datasource.common

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.NestedConfigurationProperty

data class JpaDataSourceProperty(
    val driver: String,
    val url: String,
    val username: String,
    val password: String,
    val query: String,
    val primary: Boolean,
    val packageScan: String,
    @NestedConfigurationProperty
    val hikari: HikariConfig
) {
    /**
     * 数据源配置参数校验
     */
    fun validate(): Boolean {
        var validateFlag = true
        when {
            this.url.isEmpty() -> validateFlag = false
            this.username.isEmpty() -> validateFlag = false
            this.password.isEmpty() -> validateFlag = false
            this.driver.isEmpty() -> validateFlag = false
            null == this.primary -> validateFlag = false
            null == this.hikari -> validateFlag = false
        }
        return validateFlag
    }
}
