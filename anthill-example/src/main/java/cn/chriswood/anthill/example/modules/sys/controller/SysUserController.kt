//package cn.chriswood.anthill.example.modules.sys.controller
//
//import cn.chriswood.anthill.example.persistence.primary.entity.SysUserEntity
//import cn.chriswood.anthill.example.persistence.primary.repository.SysUserRepository
//import cn.chriswood.anthill.infrastructure.web.core.BaseController
//import cn.chriswood.anthill.infrastructure.web.core.R
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/other")
//class SysUserController(
//    private val repository: SysUserRepository
//) : BaseController {
//    @GetMapping("/user/list")
//    fun list(): R<List<SysUserEntity>> {
//        return R.ok(repository.findAll())
//    }
//}
