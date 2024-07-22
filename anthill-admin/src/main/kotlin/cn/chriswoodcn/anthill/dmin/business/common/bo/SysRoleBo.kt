package com.taotao.bmm.business.common.bo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.validate.group.*
import com.taotao.bmm.support.RequiredOnCondition
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "SysRoleBo", name = "SysRoleBo")
@NoArgs
@AllOpen
@RequiredOnCondition(groups = [AddGroup::class, UpdateGroup::class])
data class SysRoleBo(
    @field:Schema(title = "主键")
    @field:NotNull(groups = [UpdateGroup::class], message = "{Validation.NotNull}")
    var id: Int?,
    @field:Schema(title = "角色名称")
    var roleNameJson: String?,
    @field:Schema(title = "角色key")
    var roleKey: String?,
    @field:Schema(title = "排序")
    var roleSort: Int?,
    @field:Schema(title = "菜单状态")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-3]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "客户ID")
    @RequiredOnCondition.RequiredValue(
        conditions = "{affiliateFlag}=='2'",
    )
    var comId: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?,
    @field:Schema(title = "所属角色")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-2|T]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var affiliateFlag: String?,
): BaseBo()