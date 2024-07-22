package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysConfigVo", name = "SysConfigVo")
@NoArgs
@AllOpen
data class SysConfigVo(
    @field:Schema(title = "主键")
    var id: Int,
    @field:Schema(title = "参数名称")
    var configNameJson: String?,
    @field:Schema(title = "键")
    var configKey: String?,
    @field:Schema(title = "值")
    var configValue: String?,
    @field:Schema(title = "状态")
    var status: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
)