package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysDictTypeVo", name = "SysDictTypeVo")
@NoArgs
@AllOpen
data class SysDictTypeVo(
    @field:Schema(title = "主键")
    var id: Int,
    @field:Schema(title = "字典类型名称")
    val dictNameJson: String?,
    @field:Schema(title = "字典类型")
    val dictType: String?,
    @field:Schema(title = "状态")
    val status: String?,
    @field:Schema(title = "备注")
    val remarkJson: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
)
