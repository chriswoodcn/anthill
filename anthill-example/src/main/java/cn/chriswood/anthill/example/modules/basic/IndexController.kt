package cn.chriswood.anthill.example.modules.basic

import cn.chriswood.anthill.infrastructure.core.config.ApplicationConfig
import cn.chriswood.anthill.infrastructure.web.aliyun.support.OssStsPool
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.MessageFormat

@RestController
@RequestMapping("/")
@Validated
class IndexController(private val applicationConfig: ApplicationConfig) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/")
    fun index(): String {
        return MessageFormat.format(
            "欢迎使用{0}后台系统,当前版本：v{1},请通过前端地址访问.",
            applicationConfig.name,
            applicationConfig.version
        )
    }

    @GetMapping("/aliyun-oss-sts/{region}")
    @SaIgnore
    fun sts(@NotBlank @PathVariable region: String): R<Any> {
        val sts = OssStsPool.getSts(region)
        return R.ok(sts?.credentials)
    }
}
