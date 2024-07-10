//package cn.chriswood.anthill.example.modules.sys
//
//import cn.chriswood.anthill.example.mybatisflex.dto.SysUserDto
//import cn.chriswood.anthill.example.mybatisflex.dto.table.SysUserTableDef.SYS_USER
//import cn.chriswood.anthill.example.mybatisflex.mapper.SysUserMapper
//import cn.chriswood.anthill.infrastructure.web.base.R
//import cn.dev33.satoken.annotation.SaIgnore
//import com.mybatisflex.core.query.QueryWrapper
//import jakarta.annotation.Resource
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/sys")
//class SysUserFlexController(
//    @Resource
//    val sysUserMapper: SysUserMapper
//) {
//
//    @GetMapping("/user/1")
//    @SaIgnore
//    fun one(): R<SysUserDto> {
//        return R.ok(sysUserMapper.selectOneById(1))
//    }
//
//    @GetMapping("/user/list")
//    @SaIgnore
//    fun list(): R<List<SysUserDto>> {
//        val queryWrapper: QueryWrapper = QueryWrapper.create()
//            .select()
//            .where(SYS_USER.USER_ID.ge(1))
//        val list: List<SysUserDto> = sysUserMapper.selectListByQuery(queryWrapper)
//        return R.ok(list)
//    }
//}
