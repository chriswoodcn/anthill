package cn.chriswood.anthill.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


// 使用多个数据源或动态数据源 必须关闭DataSourceAutoConfiguration
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
