package cn.chriswoodcn.anthill.dmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableJpaAuditing
@EnableAsync
class AnthillAdminApplication

fun main(args: Array<String>) {
    runApplication<AnthillAdminApplication>(*args)
}
