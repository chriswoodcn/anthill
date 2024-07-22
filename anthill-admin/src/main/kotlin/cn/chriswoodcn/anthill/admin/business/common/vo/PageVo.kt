package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "PageVo", name = "PageVo")
@NoArgs
@AllOpen
data class PageVo<T>(
    @field:Schema(title = "总条数")
    var totalCount: Long?,
    @field:Schema(title = "每页记录数")
    var pageSize: Int?,
    @field:Schema(title = "当前页")
    var pageNum: Int?,
    @field:Schema(title = "总页数")
    var totalPage: Int?,
    @field:Schema(title = "列表数据")
    var list: List<T>?
)


