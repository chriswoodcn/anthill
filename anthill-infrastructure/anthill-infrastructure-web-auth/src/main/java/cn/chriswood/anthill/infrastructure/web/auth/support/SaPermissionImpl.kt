package cn.chriswood.anthill.infrastructure.web.auth.support

import StpKit
import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpUtil
import org.slf4j.LoggerFactory

class SaPermissionImpl : StpInterface {
    private val log = LoggerFactory.getLogger(javaClass)
    override fun getPermissionList(p0: Any?, p1: String?): List<String> {
        log.trace("getPermissionList p0 = {} p1={}", p0, p1)
        if (UserType.SYS_USER.code == p1) {
            val loginUser = AuthHelper.getAuthUser(StpKit.SysUser)
            log.trace("StpInterface getPermissionList  permissions= {}", loginUser.permissions)
            return loginUser.permissions.toList()
        } else if (UserType.APP_USER.code == p1) {
            val loginUser = AuthHelper.getAuthUser(StpKit.AppUser)
            log.trace("StpInterface getPermissionList  permissions= {}", loginUser.permissions)
            return loginUser.permissions.toList()
        } else {

        }
        return mutableListOf()
    }

    override fun getRoleList(p0: Any?, p1: String?): List<String> {
        log.trace("getRoleList p0 = {} p1={}", p0, p1)
        val loginType = StpUtil.getLoginType()
        log.trace("StpInterface getRoleList loginType = {}", loginType)
        if (UserType.SYS_USER.code == p1) {
            val loginUser = AuthHelper.getAuthUser(StpKit.SysUser)
            log.trace("StpInterface getRoleList roles = {}", loginUser.roles)
            return loginUser.roles.toList()
        } else if (UserType.APP_USER.code == p1) {
            // 其他端 自行根据业务编写
            val loginUser = AuthHelper.getAuthUser(StpKit.AppUser)
            log.trace("StpInterface getRoleList roles = {}", loginUser.roles)
            return loginUser.permissions.toList()
        } else {

        }
        return mutableListOf()
    }
}
