package com.taotao.bmm.business.common.vo

import cn.chriswood.anthill.infrastructure.core.annotation.AllOpen
import cn.chriswood.anthill.infrastructure.core.annotation.NoArgs
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "SysCompanyVo", name = "SysCompanyVo")
@NoArgs
@AllOpen
data class SysCompanyVo(
    @field:Schema(title = "主键")
    var id: String,
    @field:Schema(title = "客户名称")
    var companyNameJson: String?,
    @field:Schema(title = "状态")
    var status: String?,
    @field:Schema(title = "模板ID")
    var templateId: Int?,
    @field:Schema(title = "到期时间")
    var activeTime: Long?,
    @field:Schema(title = "模板名称")
    var templateNameJson: String?,
    @field:Schema(title = "创建时间")
    var createTime: Long?,
    @field:Schema(title = "更新时间")
    var updateTime: Long?,
)