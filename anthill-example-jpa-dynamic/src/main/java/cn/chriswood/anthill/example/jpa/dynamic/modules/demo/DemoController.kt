package cn.chriswood.anthill.example.jpa.dynamic.modules.demo

import cn.chriswood.anthill.example.jpa.dynamic.persistence.entity.TUserEntity
import cn.chriswood.anthill.example.jpa.dynamic.persistence.repository.TUserRepository
import cn.chriswood.anthill.infrastructure.jpa.dynamic.DDS
import cn.chriswood.anthill.infrastructure.web.base.R
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController(
    val userRepository: TUserRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)


    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_primary")
    @DDS
    fun listPrimary(): R<List<TUserEntity>> {
        log.info("从主数据库读取数据")
        return R.ok(userRepository.findAll())
    }

    @Operation(summary = "请求jpa数据")
    @GetMapping("/list_slave1")
    @DDS("slave1")
    fun listSlave1(): R<List<TUserEntity>> {
        log.info("从slave1数据库读取数据")
        return R.ok(userRepository.findAll())
    }
}
