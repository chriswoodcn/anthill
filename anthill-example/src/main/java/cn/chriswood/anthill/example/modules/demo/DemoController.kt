package cn.chriswood.anthill.example.modules.demo

import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

class PostData {
    @field:NotBlank
    lateinit var username: String
    @field:NotBlank
    lateinit var password: String
}

@RestController
@RequestMapping("/demo")
class DemoController {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{id}")
    @SaIgnore
    fun id(@PathVariable id: Long): R<Long> {
        return R.ok(id)
    }

    @PostMapping("/post")
    @SaIgnore
    fun postData(@RequestBody data: PostData): R<PostData> {
        return R.ok(data)
    }
}
