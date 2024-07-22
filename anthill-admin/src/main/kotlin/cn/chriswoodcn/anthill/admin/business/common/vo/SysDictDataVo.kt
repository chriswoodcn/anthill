package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysDictDataVo", name = "SysDictDataVo")
@NoArgs
@AllOpen
data class SysDictDataVo(
    @field:Schema(title = "主键")
    val id: Int?,
    @field:Schema(title = "排序")
    val dictSort: Int?,
    @field:Schema(title = "字典标签名称")
    val dictLabelJson: String?,
    @field:Schema(title = "字典值")
    val dictValue: String?,
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
