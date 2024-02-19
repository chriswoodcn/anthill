package cn.chriswood.anthill.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication


//使用动态数据源  适用同一个数据库结构 主从库切换
//@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
//@EnableJpaRepositories
//@EnableAspectJAutoProxy
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
