package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "BackendLoginVo", name = "BackendLoginVo")
@NoArgs
@AllOpen
data class BackendLoginVo(
    @field:Schema(title = "token")
    var token: String?,
    @field:Schema(title = "用户信息")
    var info: SysUserVo,
    @field:Schema(title = "用户角色")
    var roles: Set<Int> = mutableSetOf(),
    @field:Schema(title = "用户权限")
    var permissions: Set<String> = mutableSetOf()
)