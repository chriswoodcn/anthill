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

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param authUser 登录用户信息
     * @param saLoginModel     配置参数
     */
    fun login(authUser: AuthUser, saLoginModel: SaLoginModel) {
        val storage = SaHolder.getStorage()
        storage[LOGIN_USER_KEY] = authUser
        val model = ObjectUtil.defaultIfNull(saLoginModel, SaLoginModel())
        //satoken真实登录方法
        StpUtil.login(
            authUser.getAuthLabel(),
            model
                .setExtra(USER_KEY, authUser.userId)
        )
        StpUtil.getTokenSession()[LOGIN_USER_KEY] = authUser
    }

    /**
     * 获取用户(多级缓存)
     */
    fun getAuthUser(): AuthUser? {
        return getStorageIfAbsentSet(LOGIN_USER_KEY,
            Supplier getStorageIfAbsentSet@{
                val session = StpUtil.getTokenSession()
                if (ObjectUtil.isNull(session)) {
                    return@getStorageIfAbsentSet null
                }
                session[LOGIN_USER_KEY]
            }) as AuthUser?
    }

    fun getUserType(): UserType? {
        val loginUser: AuthUser = getAuthUser() ?: return null
        return UserType.getEnumByCode(loginUser.userType)
    }

    fun getUserId(): Long? {
        val loginUser: AuthUser = getAuthUser() ?: return null
        return loginUser.userId
    }

    fun getExtra(key: String): Any? {
        return getStorageIfAbsentSet(key) {
            StpUtil.getExtra(
                key
            )
        }
    }

    fun getUsername(): String? {
        val loginUser: AuthUser = getAuthUser() ?: return null
        return loginUser.username
    }

    fun isLogin(): Boolean {
        return getAuthUser() != null
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
}
