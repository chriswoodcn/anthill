package cn.chriswood.anthill.example.modules.sys.controller

import cn.chriswood.anthill.example.persistence.other.entity.OtherSysUserEntity
import cn.chriswood.anthill.example.persistence.other.repository.OtherSysUserRepository
import cn.chriswood.anthill.infrastructure.web.core.BaseController
import cn.chriswood.anthill.infrastructure.web.core.R
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/other")
class OtherSysUserController(
    private val repository: OtherSysUserRepository
) : BaseController {
    @GetMapping("/user/list")
    fun list(): R<List<OtherSysUserEntity>> {
        return R.ok(repository.findAll())
    }
}
