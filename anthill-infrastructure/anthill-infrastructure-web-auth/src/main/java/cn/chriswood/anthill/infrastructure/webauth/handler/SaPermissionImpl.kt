package cn.chriswood.anthill.infrastructure.webauth.handler

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.stp.StpInterface

class SaPermissionImpl : StpInterface {
    override fun getPermissionList(p0: Any?, p1: String?): MutableList<String> {
        val userType = AuthHelper.getUserType()
        if (userType === UserType.SYS_USER) {
            val loginUser = AuthHelper.getAuthUser() ?: return mutableListOf()
            return loginUser.menuPermission.toMutableList()
        } else if (userType === UserType.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return mutableListOf()
    }

    override fun getRoleList(p0: Any?, p1: String?): MutableList<String> {
        val userType = AuthHelper.getUserType()
        if (userType === UserType.SYS_USER) {
            val loginUser = AuthHelper.getAuthUser() ?: return mutableListOf()
            return loginUser.rolePermission.toMutableList()
        } else if (userType === UserType.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return mutableListOf()
    }
}
