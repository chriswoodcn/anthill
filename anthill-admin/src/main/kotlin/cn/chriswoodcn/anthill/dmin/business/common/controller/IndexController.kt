package com.taotao.bmm.business.common.controller

import cn.chriswood.anthill.infrastructure.core.config.ApplicationConfig
import cn.dev33.satoken.annotation.SaIgnore
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "v1-common")
class IndexController(private val applicationConfig: ApplicationConfig) {

    @Operation(summary = "首页提示语")
    @SaIgnore
    @GetMapping("/")
    fun index(): String {
        return """
            欢迎使用${applicationConfig.name}后台管理框架，
            当前版本：v${applicationConfig.version}，
            请通过前端地址访问。
            """.trimIndent()
    }
}



