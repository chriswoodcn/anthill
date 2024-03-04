package cn.chriswood.anthill.infrastructure.jpa.multi

import com.zaxxer.hikari.HikariConfig
import org.springframework.util.Assert

data class MultiDataSourceProperty(
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
     * 扫描的包名
     */
    val packageScan: String,
    /**
     * db init query
     */
    val query: String,
    /**
     * db dialect class
     */
    val dialect: String,
    /**
     * HikariConfig
     */
    var hikari: HikariConfig = HikariConfig()
) {
    /**
     * 数据源配置参数校验
     */
    fun validate() {
        Assert.hasText(driver, "driver not be null")
        Assert.hasText(url, "url not be null")
        Assert.hasText(username, "username not be null")
        Assert.hasText(password, "password not be null")
        Assert.hasText(packageScan, "packageScan not be null")
        Assert.hasText(query, "query not be null")
        Assert.hasText(dialect, "dialect not be null")
    }
}
