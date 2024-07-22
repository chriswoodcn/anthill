package cn.chriswood.anthill.example.modules.demo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "PostData", name = "PostData")
class PostData {
    @field:Schema(description = "示例post请求username")
    @field:NotBlank
    lateinit var username: String

    @field:Schema(description = "示例post请求password")
    @field:NotBlank
    lateinit var password: String
}
