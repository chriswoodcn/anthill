package cn.chriswood.anthill.example.jpa.multi.modules.demo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "PostData", name = "PostData")
data class PostData(
    @field:Schema(description = "示例post请求username")
    @field:NotBlank
    var username: String,

    @field:Schema(description = "示例post请求password")
    @field:NotBlank
    var password: String
)
