package com.taotao.bmm.business.backend.controller

import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import com.taotao.bmm.business.common.form.BackendLoginForm
import com.taotao.bmm.business.common.service.SysLoginService
import com.taotao.bmm.business.common.vo.BackendLoginVo
import com.taotao.bmm.business.common.vo.SysMenuVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/backend")
@Tag(name = "v1-backend-登录接口")
class SysLoginController(
    private val sysLoginService: SysLoginService
) {
    @Operation(summary = "登录")
    @PostMapping("/login")
    @SaIgnore
    fun login(@Valid @RequestBody form: BackendLoginForm): R<BackendLoginVo> {
        return R.ok(sysLoginService.login(form))
    }

    @Operation(summary = "登录用户信息")
    @GetMapping("/userInfo")
    @SaIgnore
    fun userInfo(): R<BackendLoginVo> {
        return R.ok(sysLoginService.userInfo())
    }

    @Operation(summary = "登出")
    @GetMapping("/logout")
    fun logout(): R<Unit> {
        AuthHelper.logout()
        return R.ok()
    }

    @Operation(summary = "获取菜单路由")
    @PostMapping("/getRouters")
    fun getRouters(): R<List<SysMenuVo>> {
        return R.ok(sysLoginService.getRouters())
    }
}