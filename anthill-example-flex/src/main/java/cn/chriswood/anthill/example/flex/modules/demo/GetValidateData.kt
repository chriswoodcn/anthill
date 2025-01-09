package cn.chriswood.anthill.example.flex.modules.demo

import cn.chriswood.anthill.infrastructure.core.validate.group.QueryGroup
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(name = "GetValidateData", description = "GetValidateData")
data class GetValidateData(
    @field:NotBlank(groups = [QueryGroup::class])
    @Schema(description = "昵称")
    val nickname: String? = null,
    @Schema(description = "头像")
    val avatar: String? = null,
)
