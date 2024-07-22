package com.taotao.bmm.business.common.bo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import cn.chriswood.anthill.infrastructure.core.validate.group.AddGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.AllGroup
import cn.chriswood.anthill.infrastructure.core.validate.group.UpdateGroup
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "SysConfigBo", name = "SysConfigBo")
@NoArgs
@AllOpen
data class SysConfigBo(
    @field:Schema(title = "主键")
    @field:NotNull(groups = [UpdateGroup::class], message = "{Validation.NotNull}")
    var id: Int?,
    @field:Schema(title = "参数名称")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var configNameJson: String?,
    @field:Schema(title = "键")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var configKey: String?,
    @field:Schema(title = "值")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var configValue: String?,
    @field:Schema(title = "状态")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-3]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?
) : BaseBo()