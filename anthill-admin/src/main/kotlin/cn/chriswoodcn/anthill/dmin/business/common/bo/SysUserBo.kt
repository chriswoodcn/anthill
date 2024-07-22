package com.taotao.bmm.business.common.bo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.validate.PhoneNumber
import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.AllGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import com.taotao.bmm.support.Constants
import com.taotao.bmm.support.RequiredOnCondition
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "SysUserBo", name = "SysUserBo")
@NoArgs
@AllOpen
@RequiredOnCondition(groups = [AddGroup::class])
data class SysUserBo(
    @field:Schema(title = "主键")
    @field:NotNull(groups = [UpdateGroup::class], message = "{Validation.NotNull}")
    var id: Int?,
    @field:Schema(title = "登录账户")
    var username: String?,
    @field:Schema(title = "昵称")
    var nickname: String?,
    @field:Schema(title = "用户类型", defaultValue = "0")
    @field:Pattern(
        regexp = "[0-2]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var userType: String?,
    @field:Schema(title = "邮箱")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Email(groups = [AllGroup::class], message = "{Validation.Invalid}")
    var email: String?,
    @field:Schema(title = "电话")
    @field:PhoneNumber(groups = [AllGroup::class], message = "{Validation.Invalid}")
    var mobile: String?,
    @field:Schema(title = "性别", defaultValue = "2")
    @field:Pattern(
        regexp = "[0-2]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var sex: String?,
    @field:Schema(title = "头像")
    var avatar: String?,
    @field:Schema(title = "密码")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = Constants.REGEX_BACKEND_PASSWORD,
        groups = [AddGroup::class],
        message = "{Validation.Pattern}"
    )
    var password: String?,
    @field:Schema(title = "状态", defaultValue = "0")
    @field:Pattern(
        regexp = "[0-2]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "登录IP")
    var loginIp: String?,
    @field:Schema(title = "登录时间")
    var loginTime: Long?,
    @field:Schema(title = "公司ID")
    @RequiredOnCondition.RequiredValue(
        conditions = "{userType}=='2'",
    )
    var comId: String?,
    @field:Schema(title = "是否管理员", defaultValue = "0")
    @field:Pattern(
        regexp = "[0-1]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var adminFlag: String?,
) : BaseBo()