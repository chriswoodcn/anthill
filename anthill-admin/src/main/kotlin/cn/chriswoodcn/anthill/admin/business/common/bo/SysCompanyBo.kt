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

@Schema(description = "SysCompanyBo", name = "SysCompanyBo")
@NoArgs
@AllOpen
data class SysCompanyBo(
    @field:Schema(title = "主键")
    @field:NotBlank(groups = [UpdateGroup::class, AddGroup::class], message = "{Validation.NotBlank}")
    var id: String?,
    @field:Schema(title = "客户名称")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    var companyNameJson: String?,
    @field:Schema(title = "状态")
    @field:NotBlank(groups = [AddGroup::class], message = "{Validation.NotBlank}")
    @field:Pattern(
        regexp = "[0-2]",
        groups = [AllGroup::class],
        message = "{Validation.Pattern}"
    )
    var status: String?,
    @field:Schema(title = "模板ID")
    @field:NotNull(groups = [AddGroup::class], message = "{Validation.NotNull}")
    var templateId: Int?,
    @field:Schema(title = "到期时间")
    @field:NotNull(groups = [AddGroup::class], message = "{Validation.NotNull}")
    var activeTime: Long?,
) : BaseBo()