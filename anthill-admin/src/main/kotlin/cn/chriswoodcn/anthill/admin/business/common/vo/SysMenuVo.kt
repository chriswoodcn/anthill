package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysMenuVo", name = "SysMenuVo")
@NoArgs
@AllOpen
data class SysMenuVo(
    @field:Schema(title = "主键")
    var id: Int,
    @field:Schema(title = "菜单名称")
    var menuNameJson: String?,
    @field:Schema(title = "父级节点")
    var parentId: Int?,
    @field:Schema(title = "排序")
    var orderNum: Int?,
    @field:Schema(title = "路由路径")
    var path: String?,
    @field:Schema(title = "文件路径")
    var component: String?,
    @field:Schema(title = "菜单类型")
    var menuType: String?,
    @field:Schema(title = "菜单状态")
    var status: String?,
    @field:Schema(title = "权限字符")
    var perms: String?,
    @field:Schema(title = "图标")
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
    var affiliateFlag: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
    @field:Schema(title = "子级菜单")
    var children: List<SysMenuVo>?,
)
