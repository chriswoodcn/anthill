package cn.chriswood.anthill.example.flex

import cn.chriswood.anthill.infrastructure.mail.support.MailPool
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan(basePackages = ["cn.chriswood.anthill.example.flex.persistence"])
class ExampleFlexApplication

fun main(args: Array<String>) {
    runApplication<ExampleFlexApplication>(*args)
    MailPool.getAvailableMailAccount("-1", "-1")
}
