package cn.chriswood.anthill.infrastructure.datasource.dynamic

import com.zaxxer.hikari.HikariConfig

data class DynamicDataSourceProperty(
    val driver: String,
    val url: String,
    val username: String,
    val password: String,
    val query: String?,
    var hikari: HikariConfig = HikariConfig()
) {
    /**
     * 数据源配置参数校验
     */
    fun validate(): Boolean {
        var validateFlag = true
        when {
            this.driver.isEmpty() -> validateFlag = false
            this.url.isEmpty() -> validateFlag = false
            this.username.isEmpty() -> validateFlag = false
            this.password.isEmpty() -> validateFlag = false
        }
        return validateFlag
    }
}
