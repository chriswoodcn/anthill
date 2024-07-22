package com.taotao.bmm.business.common.form

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode
import jakarta.validation.constraints.NotBlank

@Schema(description = "BackendLoginForm", name = "BackendLoginForm")
class BackendLoginForm {
    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    @field:NotBlank(message = "{Validation.NotBlank}")
    lateinit var username: String

    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    @field:NotBlank(message = "{Validation.NotBlank}")
    lateinit var password: String
}