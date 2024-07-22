package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysRoleVo", name = "SysRoleVo")
@NoArgs
@AllOpen
data class SysRoleVo(
    @field:Schema(title = "主键")
    var id: Int,
    @field:Schema(title = "角色名称")
    var roleNameJson: String?,
    @field:Schema(title = "角色key")
    var roleKey: String?,
    @field:Schema(title = "排序")
    var roleSort: Int?,
    @field:Schema(title = "菜单状态")
    var status: String?,
    @field:Schema(title = "客户ID")
    var comId: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?,
    @field:Schema(title = "所属角色")
    var affiliateFlag: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
)