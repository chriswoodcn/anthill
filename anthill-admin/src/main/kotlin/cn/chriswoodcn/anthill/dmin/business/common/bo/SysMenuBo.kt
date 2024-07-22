package com.taotao.bmm.business.common.bo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.validate.group.*
import com.taotao.bmm.support.RequiredOnCondition
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "SysMenuBo", name = "SysMenuBo")
@NoArgs
@AllOpen
@RequiredOnCondition(groups = [AddGroup::class, UpdateGroup::class])
data class SysMenuBo(
    @field:Schema(title = "主键")
    @field:NotNull(groups = [UpdateGroup::class], message = "{Validation.NotNull}")
    var id: Int?,
    @field:Schema(title = "菜单名称")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var menuNameJson: String?,
    @field:Schema(title = "父级节点", defaultValue = "0")
    @field:NotNull(groups = [AddGroup::class], message = "{Validation.NotNull}")
    var parentId: Int?,
    @field:Schema(title = "排序", defaultValue = "0")
    @field:NotNull(groups = [AddGroup::class], message = "{Validation.NotNull}")
    var orderNum: Int?,
    @field:Schema(title = "路由路径")
    @RequiredOnCondition.RequiredValue(
        conditions = "{menuType}=='M'||{menuType}=='C'",
    )
    var path: String?,
    @field:Schema(title = "文件路径")
    var component: String?,
    @field:Schema(title = "菜单类型")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[MCF]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var menuType: String?,
    @field:Schema(title = "菜单状态")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-3]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "权限字符")
    @RequiredOnCondition.RequiredValue(
        conditions = "{menuType}=='F'||{menuType}=='C'",
    )
    var perms: String?,
    @field:Schema(title = "图标")
    @RequiredOnCondition.RequiredValue(
        conditions = "{menuType}=='M'",
    )
    var icon: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?,
    @field:Schema(title = "版本")
    var menuVersion: String?,
    @field:Schema(title = "是否隐藏")
    var hiddenFlag: String?,
    @field:Schema(title = "是否链接")
    var frameFlag: String?,
    @field:Schema(title = "所属角色")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-2]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var affiliateFlag: String?,
): BaseBo()