package cn.chriswood.anthill.infrastructure.webauth.handler

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import java.util.function.Supplier

object AuthHelper {
    val LOGIN_USER_KEY = "loginUser"
    val USER_KEY = "userId"
    val DEPT_KEY = "deptId"

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     * @param model     配置参数
     */
    fun login(authUser: AuthUser, saLoginModel: SaLoginModel) {
        val storage = SaHolder.getStorage()
        storage[LOGIN_USER_KEY] = authUser
        storage[USER_KEY] = authUser.userId
        storage[DEPT_KEY] = authUser.deptId
        val model = ObjectUtil.defaultIfNull(saLoginModel, SaLoginModel())
        //satoken真实登录方法
        StpUtil.login(
            authUser.getAuthLabel(),
            model
                .setExtra(USER_KEY, authUser.userId)
                .setExtra(DEPT_KEY, authUser.deptId)
        )
        StpUtil.getTokenSession()[LOGIN_USER_KEY] = authUser
    }

    /**
     * 获取用户(多级缓存)
     */
    fun getAuthUser(): AuthUser {
        return getStorageIfAbsentSet(LOGIN_USER_KEY,
            Supplier<Any?> getStorageIfAbsentSet@{
                val session = StpUtil.getTokenSession()
                if (ObjectUtil.isNull(session)) {
                    return@getStorageIfAbsentSet null
                }
                session[LOGIN_USER_KEY]
            }) as AuthUser
    }

    private fun getStorageIfAbsentSet(key: String, handle: Supplier<Any?>): Any? {
        return try {
            var obj = SaHolder.getStorage()[key]
            if (ObjectUtil.isNull(obj)) {
                obj = handle.get()
                if (ObjectUtil.isNotNull(obj))
                    SaHolder.getStorage()[key] = obj
            }
            obj
        } catch (e: Exception) {
            null
        }
    }

    fun getUserType(): UserType {
        val loginUser: AuthUser = getAuthUser()
        return UserType.getEnumByCode(loginUser.userType)
    }
}
