package cn.chriswood.anthill.infrastructure.datasource.multi

import com.zaxxer.hikari.HikariConfig
import org.springframework.util.Assert

data class MultiDataSourceProperty(
    val driver: String,
    val url: String,
    val username: String,
    val password: String,
    val packageScan: String,
    val query: String,
    val dialect: String,
    var hikari: HikariConfig?
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
