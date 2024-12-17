package cn.chriswood.anthill.example.flex.modules.demo

import cn.chriswood.anthill.example.mybatisflex.dto.TUserDs1Dto
import cn.chriswood.anthill.example.mybatisflex.dto.TUserDs2Dto
import cn.chriswood.anthill.infrastructure.web.base.R
import com.mybatisflex.kotlin.extensions.db.query
import com.mybatisflex.kotlin.extensions.kproperty.eq
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController {

    private val log = LoggerFactory.getLogger(javaClass)


    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_primary")
    fun listPrimary(): R<List<TUserDs1Dto>> {
        log.info("从ds1数据库读取数据")
        val accountList: List<TUserDs1Dto> = query {
            where(TUserDs1Dto::userId.eq(1))
        }
        return R.ok(accountList)
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_slave1")
    fun listSlave1(): R<List<TUserDs2Dto>> {
        log.info("从ds2数据库读取数据")
        val accountList: List<TUserDs2Dto> = query {
            where(TUserDs1Dto::userId.eq(1))
        }
        return R.ok(accountList)
    }
}
