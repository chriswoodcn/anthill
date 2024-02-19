package cn.chriswood.anthill.example

import cn.chriswood.anthill.infrastructure.datasource.dynamic.DynamicDataSourceAspect
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


//使用动态数据源  适用同一个数据库结构 主从库切换
//@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
//@EnableJpaRepositories
//@EnableAspectJAutoProxy
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableJpaRepositories
@EnableAspectJAutoProxy
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
