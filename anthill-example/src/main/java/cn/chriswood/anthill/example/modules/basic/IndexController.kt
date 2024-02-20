package cn.chriswood.anthill.example.modules.basic

import cn.chriswood.anthill.infrastructure.json.JacksonUtil
import cn.chriswood.anthill.infrastructure.spring.ApplicationConfig
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/")
    fun index(): String {
        val primary: DataSource = SpringUtil.getBean("primaryDataSource")
        log.info(primary.connection.metaData.url)
        val second: DataSource = SpringUtil.getBean("secondDataSource")
        log.info(second.connection.metaData.url)
        val primaryEntityManagerFactory: LocalContainerEntityManagerFactoryBean = SpringUtil.getBean("&primaryEntityManagerFactory")
        log.info(primaryEntityManagerFactory.toString())
        val primaryTransactionManager: JpaTransactionManager = SpringUtil.getBean("primaryTransactionManager")
        log.info(primaryTransactionManager.toString())
        val secondEntityManagerFactory: LocalContainerEntityManagerFactoryBean = SpringUtil.getBean("&secondEntityManagerFactory")
        log.info(secondEntityManagerFactory.toString())
        val secondTransactionManager: JpaTransactionManager = SpringUtil.getBean("secondTransactionManager")
        log.info(secondTransactionManager.toString())
        return MessageFormat.format(
            "欢迎使用{0}后台系统,当前版本：v{1},请通过前端地址访问.",
            applicationConfig.name,
            applicationConfig.version
        )
    }
}
