package cn.chriswood.anthill.example.flex.modules.demo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(name = "PageForm", description = "PageForm")
data class PageForm(
    @Schema(description = "页码")
    @Min(1)
    val pageNum: Int,
    @Schema(description = "每页记录")
    @Min(1)
    val pageSize: Int
)
