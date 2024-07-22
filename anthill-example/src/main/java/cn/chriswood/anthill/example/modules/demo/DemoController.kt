package cn.chriswood.anthill.example.modules.demo

import cn.chriswood.anthill.infrastructure.web.base.R
import cn.dev33.satoken.annotation.SaIgnore
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody


@RestController
@RequestMapping("/demo")
class DemoController {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "简单get请求示例")
    @GetMapping("/{id}")
    @SaIgnore
    fun id(@PathVariable id: Long): R<Long> {
        return R.ok(id)
    }

    @Operation(summary = "简单post请求示例")
    @PostMapping("/post")
    @SaIgnore
    fun postData(@RequestBody data: PostData): R<PostData> {
        return R.ok(data)
    }

    @Operation(summary = "stream流导出大量数据示例")
    @PostMapping("/export")
    @SaIgnore
    fun exportData(): ResponseEntity<StreamingResponseBody> {
        return ResponseEntity(StreamingResponseBody {
            // 从数据库获取到stream数据后向OutputStream写数据即可
        }, HttpStatus.OK)
    }
}
