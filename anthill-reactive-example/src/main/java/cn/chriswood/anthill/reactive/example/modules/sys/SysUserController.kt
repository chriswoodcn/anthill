package cn.chriswood.anthill.reactive.example.modules.sys

import cn.chriswood.anthill.reactive.example.modules.vo.SysUserVo
import cn.chriswood.anthill.reactive.example.persistence.r2dbc.repo.SysUserRepo
import cn.hutool.core.bean.BeanUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sys/user")
@Validated
class SysUserController(
    private val repo: SysUserRepo
) {
    @GetMapping("/list")
    suspend fun listSysUser(): Flow<SysUserVo> {
        return repo.findAll().map {
            BeanUtil.copyProperties(it, SysUserVo::class.java)
        }
    }
}
