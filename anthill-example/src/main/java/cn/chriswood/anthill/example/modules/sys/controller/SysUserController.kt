package cn.chriswood.anthill.example.modules.sys

import cn.chriswood.anthill.example.mybatisflex.dto.SysUserDto
import cn.chriswood.anthill.example.mybatisflex.mapper.SysUserMapper
import jakarta.annotation.Resource
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


//package cn.chriswood.anthill.example.modules.sys.controller
//
//import cn.chriswood.anthill.example.persistence.primary.entity.SysUserEntity
//import cn.chriswood.anthill.example.persistence.primary.repository.SysUserRepository
//import cn.chriswood.anthill.infrastructure.web.annotation.repeatLimit.RepeatLimit
//import cn.chriswood.anthill.infrastructure.web.base.BaseController
//import cn.chriswood.anthill.infrastructure.web.base.R
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/sys")
//class SysUserController(
//    private val repository: SysUserRepository
//) : BaseController {
//    @GetMapping("/user/list")
//    @RepeatLimit
//    fun list(): R<List<SysUserEntity>> {
//        return R.ok(repository.findAll())
//    }
//}

@RestController
@RequestMapping("/sys")
class SysUserController(
    @Resource
    @Lazy
    val sysUserMapper: SysUserMapper
) {

    @GetMapping("/user/list")
    fun list(): SysUserDto {
//        val queryWrapper: QueryWrapper = QueryWrapper.create()
//            .select()
//            .where(SYS_USER_DTO.USER_ID.eq(1))
//        val dto: SysUserDto = sysUserMapper.selectOneByQuery(queryWrapper)
        return sysUserMapper.selectOneById(1)
    }
}
