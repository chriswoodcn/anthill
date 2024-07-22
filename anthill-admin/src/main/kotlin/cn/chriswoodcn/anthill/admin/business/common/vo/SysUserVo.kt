package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysUserVo", name = "SysUserVo")
@NoArgs
@AllOpen
data class SysUserVo(
    @field:Schema(title = "主键")
    var id: Int,
    @field:Schema(title = "登录账户")
    var username: String?,
    @field:Schema(title = "昵称")
    var nickname: String?,
    @field:Schema(title = "用户类型")
    var userType: String?,
    @field:Schema(title = "邮箱")
    var email: String?,
    @field:Schema(title = "电话")
    var mobile: String?,
    @field:Schema(title = "性别")
    var sex: String?,
    @field:Schema(title = "头像")
    var avatar: String?,
    @field:Schema(title = "密码")
    var password: String?,
    @field:Schema(title = "状态")
    var status: String?,
    @field:Schema(title = "登录IP")
    var loginIp: String?,
    @field:Schema(title = "登录时间")
    var loginTime: Long?,
    @field:Schema(title = "公司ID")
    var comId: String?,
    @field:Schema(title = "是否管理员")
    var adminFlag: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
)