package cn.chriswood.anthill.example.flex.modules.demo

import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length

@Schema(name = "PostValidateData", description = "PostValidateData")
data class PostValidateData(
    @field:NotBlank(groups = [AddGroup::class, UpdateGroup::class])
    @Schema(
        description = "姓名",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val name: String? = null,
    @field:Min(1)
    @field:Max(200)
    @field:NotNull
    @Schema(
        description = "年龄", maximum = "200", minimum = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val age: Int? = null,
    @field:NotBlank(groups = [AddGroup::class])
    @field:Pattern(regexp = "[0-2]")
    @Schema(
        description = "性别", pattern = "[0-2]",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val sex: String? = null,
    @field:Length(max = 60)
    @Schema(description = "简介", maxLength = 60)
    val profile: String? = null,
)
