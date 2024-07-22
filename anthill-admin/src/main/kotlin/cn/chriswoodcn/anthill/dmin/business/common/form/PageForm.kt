package com.taotao.bmm.business.common.form

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Schema(description = "PageForm", name = "PageForm")
open class PageForm {

    @Schema(title = "当前页数", defaultValue = "1")
    @Min(value = 1)
    val pageNum: Int = 1

    @Schema(title = "每页记录数", defaultValue = "10")
    @Min(value = 1)
    @Max(value = 999)
    val pageSize: Int = 10
}
