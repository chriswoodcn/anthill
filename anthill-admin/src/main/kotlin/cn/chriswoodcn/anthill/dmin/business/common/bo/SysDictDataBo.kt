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

@Schema(description = "SysDictDataBo", name = "SysDictDataBo")
@NoArgs
@AllOpen
data class SysDictDataBo(
    @field:Schema(title = "主键")
    @field:NotNull(groups = [UpdateGroup::class], message = "{Validation.NotNull}")
    var id: Int?,
    @field:Schema(title = "排序")
    @field:NotNull(groups = [AddGroup::class], message = "{Validation.NotNull}")
    var dictSort: Int?,
    @field:Schema(title = "字典标签名称")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var dictLabelJson: String?,
    @field:Schema(title = "字典值")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var dictValue: String?,
    @field:Schema(title = "字典类型")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var dictType: String?,
    @field:Schema(title = "状态")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-3]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "备注")
    var remarkJson: String?,

    ) : BaseBo()