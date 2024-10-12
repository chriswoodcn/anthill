package cn.chriswood.anthill.infrastructure.web.auth.support

import StpKit
import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpUtil

class SaPermissionImpl : StpInterface {
    override fun getPermissionList(p0: Any?, p1: String?): MutableList<String> {
        val loginType = StpUtil.getLoginType()
        if (loginType == UserType.SYS_USER.code) {
            val loginUser = AuthHelper.getAuthUser(StpKit.SysUser)
            return loginUser.permissions.toMutableList()
        } else if (loginType == UserType.APP_USER.code) {
            // 其他端 自行根据业务编写
        }
        return mutableListOf()
    }

    override fun getRoleList(p0: Any?, p1: String?): MutableList<String> {
        val loginType = StpUtil.getLoginType()
        if (loginType == UserType.SYS_USER.code) {
            val loginUser = AuthHelper.getAuthUser(StpKit.SysUser)
            return loginUser.roles.toMutableList()
        } else if (loginType == UserType.APP_USER.code) {
            // 其他端 自行根据业务编写
        }
        return mutableListOf()
    }
}
