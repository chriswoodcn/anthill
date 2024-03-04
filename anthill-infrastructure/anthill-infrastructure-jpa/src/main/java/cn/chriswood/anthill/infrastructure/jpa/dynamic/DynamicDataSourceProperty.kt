package cn.chriswood.anthill.infrastructure.jpa.dynamic

import com.zaxxer.hikari.HikariConfig
data class DynamicDataSourceProperty(
    /**
     * driver class name
     */
    val driver: String,
    /**
     * db url
     */
    val url: String,
    /**
     * db username
     */
    val username: String,
    /**
     * db password
     */
    val password: String,
    /**
     * db init query
     */
    val query: String?,
    /**
     * HikariConfig
     */
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
