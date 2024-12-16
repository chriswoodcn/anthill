package cn.chriswood.anthill.example.jpa.dynamic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ExampleJpaDynamicApplication

fun main(args: Array<String>) {
    runApplication<ExampleJpaDynamicApplication>(*args)
}
