package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import java.util.function.Supplier

object AuthHelper {
    const val LOGIN_USER_KEY = "loginUser"
    const val USER_KEY = "userId"

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param authUser 登录用户信息
     * @param saLoginModel     配置参数
     */
    fun <T> login(authUser: AuthUser<T>, saLoginModel: SaLoginModel) {
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

    fun logout() {
        if (isLogin()) StpUtil.logout()
    }

    private fun getStoreUser(): Any? {
        return getStorageIfAbsentSet(
            LOGIN_USER_KEY,
            Supplier getStorageIfAbsentSet@{
                val session = StpUtil.getTokenSession()
                if (ObjectUtil.isNull(session)) {
                    return@getStorageIfAbsentSet null
                }
                session[LOGIN_USER_KEY]
            })
    }

    /**
     * 获取用户(多级缓存)
     */
    fun getAuthUser(): AuthUser<*> {
        val getObj = getStoreUser() ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
        return getObj as AuthUser<*>
    }

    fun getToken(): String {
        return StpUtil.getTokenValue() ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
    }

    fun getUserType(): UserType {
        val loginUser: AuthUser<*> = getAuthUser()
        return UserType.getEnumByCode(loginUser.userType)
    }

    fun getUserId(): String {
        val loginUser: AuthUser<*> = getAuthUser()
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
        val loginUser: AuthUser<*> = getAuthUser()
        return loginUser.username
    }

    fun isLogin(): Boolean {
        return StpUtil.isLogin()
    }

    fun isLogin(loginId: Any): Boolean {
        return StpUtil.isLogin(loginId)
    }

    fun <T> refreshUser(loginId: Any, data: AuthUser<T>) {
        val myLoginId = StpUtil.getLoginId()
        if (myLoginId == loginId) {
            SaHolder.getStorage().set(LOGIN_USER_KEY, data)
            StpUtil.getTokenSession()[LOGIN_USER_KEY] = data
        }
        if (isLogin(loginId)) {
            StpUtil.switchTo(loginId)
            SaHolder.getStorage().set(LOGIN_USER_KEY, data)
            StpUtil.getTokenSession()[LOGIN_USER_KEY] = data
            StpUtil.switchTo(myLoginId)
        }
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
