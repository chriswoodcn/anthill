package cn.chriswood.anthill.example.jpa.multi.modules.basic

import cn.chriswood.anthill.infrastructure.core.config.ApplicationConfig
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.MessageFormat

@RestController
@RequestMapping("/")
@Validated
class IndexController(
    private val applicationConfig: ApplicationConfig,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun index(): String {
        return MessageFormat.format(
            "欢迎使用{0}后台系统,当前版本：v{1},请通过前端地址访问.",
            applicationConfig.name,
            applicationConfig.version
        )
    }
}
