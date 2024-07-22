package com.taotao.bmm.business.common.bo

import io.swagger.v3.oas.annotations.media.Schema

open class BaseBo {
    @field:Schema(title = "query name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    var queryName: String? = null

    @field:Schema(title = "query start", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    var queryStart: Long? = null

    @field:Schema(title = "query end", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    var queryEnd: Long? = null
}