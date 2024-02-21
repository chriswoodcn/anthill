package cn.chriswood.anthill.example.modules.basic

import cn.chriswood.anthill.infrastructure.spring.ApplicationConfig
import cn.chriswood.anthill.infrastructure.web.exception.InfrastructureWebEnum
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.MessageFormat

@RestController
@RequestMapping("/")
class IndexController(private val applicationConfig: ApplicationConfig) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/")
    fun index(): String {
        InfrastructureWebEnum.FUNC_ERROR.eject("IndexController", "index", "其实并没有报错，只是测试下")
        return MessageFormat.format(
            "欢迎使用{0}后台系统,当前版本：v{1},请通过前端地址访问.",
            applicationConfig.name,
            applicationConfig.version
        )
    }
}
