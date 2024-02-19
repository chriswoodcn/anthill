package cn.chriswood.anthill.example.modules.basic

import cn.chriswood.anthill.infrastructure.spring.ApplicationConfig
import cn.hutool.extra.spring.SpringUtil
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.MessageFormat
import javax.sql.DataSource

@RestController
@RequestMapping("/")
class IndexController(private val applicationConfig: ApplicationConfig) {
    @GetMapping("/")
    fun index(): String {
        val primary: DataSource = SpringUtil.getBean("primaryDataSource")
        println(primary.connection.metaData.url)
        val second: DataSource = SpringUtil.getBean("secondDataSource")
        println(second.connection.metaData.url)
        val primaryEntityManagerFactory: LocalContainerEntityManagerFactoryBean = SpringUtil.getBean("&primaryEntityManagerFactory")
        println(primaryEntityManagerFactory)
        val primaryTransactionManager: JpaTransactionManager = SpringUtil.getBean("primaryTransactionManager")
        println(primaryTransactionManager)
        val secondEntityManagerFactory: LocalContainerEntityManagerFactoryBean = SpringUtil.getBean("&secondEntityManagerFactory")
        println(secondEntityManagerFactory)
        val secondTransactionManager: JpaTransactionManager = SpringUtil.getBean("secondTransactionManager")
        println(secondTransactionManager)
        return MessageFormat.format(
            "欢迎使用{0}后台系统,当前版本：v{1},请通过前端地址访问.",
            applicationConfig.name,
            applicationConfig.version
        )
    }
}
