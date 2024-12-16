package cn.chriswood.anthill.example.jpa.multi.modules.demo

import cn.chriswood.anthill.example.jpa.multi.persistence.primary.entity.TUserEntity
import cn.chriswood.anthill.example.jpa.multi.persistence.primary.repository.TUserRepository
import cn.chriswood.anthill.example.jpa.multi.persistence.secondary.entity.SysUserEntity
import cn.chriswood.anthill.example.jpa.multi.persistence.secondary.repository.SysUserRepository
import cn.chriswood.anthill.infrastructure.web.aliyun.support.OssStsPool
import cn.chriswood.anthill.infrastructure.web.base.R
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody


@RestController
@RequestMapping("/demo")
class DemoController(
    val userRepository: TUserRepository,
    val sysUserRepository: SysUserRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "简单get请求示例")
    @GetMapping("/{id}")
    fun id(@PathVariable id: Long): R<Long> {
        return R.ok(id)
    }

    @Operation(summary = "简单post请求示例")
    @PostMapping("/post")
    fun postData(@RequestBody data: PostData): R<PostData> {
        return R.ok(data)
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_primary")
    fun listPrimary(): R<List<TUserEntity>> {
        return R.ok(userRepository.findAll())
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_secondary")
    fun listSecondary(): R<List<SysUserEntity>> {
        return R.ok(sysUserRepository.findAll())
    }

    @GetMapping("/aliyun-oss-sts/{region}")
    fun sts(@NotBlank @PathVariable region: String?): R<Any> {
        val sts = OssStsPool.getSts(region)
        return R.ok(sts?.credentials)
    }

    @Operation(summary = "stream流导出大量数据示例")
    @PostMapping("/export")
    fun exportData(): ResponseEntity<StreamingResponseBody> {
        return ResponseEntity(StreamingResponseBody {
            // 从数据库获取到stream数据后向OutputStream写数据即可
        }, HttpStatus.OK)
    }
}
