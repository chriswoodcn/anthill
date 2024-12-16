package cn.chriswood.anthill.example.flex.modules.demo

import cn.chriswood.anthill.example.flex.persistence.TUserDs1Mapper
import cn.chriswood.anthill.example.flex.persistence.TUserDs2Mapper
import cn.chriswood.anthill.example.mybatisflex.dto.TUserDs1Dto
import cn.chriswood.anthill.example.mybatisflex.dto.TUserDs2Dto
import cn.chriswood.anthill.infrastructure.web.base.R
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController(
    val userDs1Mapper: TUserDs1Mapper,
    val userDs2Mapper: TUserDs2Mapper
) {

    private val log = LoggerFactory.getLogger(javaClass)


    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_primary")
    fun listPrimary(): R<List<TUserDs1Dto>> {
        log.info("从ds1数据库读取数据")
        return R.ok(userDs1Mapper.selectAll())
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_slave1")
    fun listSlave1(): R<List<TUserDs2Dto>> {
        log.info("从ds2数据库读取数据")
        return R.ok(userDs2Mapper.selectAll())
    }
}
