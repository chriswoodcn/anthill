package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpLogic
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import java.io.Serializable
import java.util.function.Supplier

object AuthHelper {
    const val LOGIN_USER_KEY = "loginUser"
    const val USER_KEY = "userId"

    /**
     * 获得对应的loginId
     */
    fun genLoginId(userType: String, id: Serializable): String {
        return "$userType:$id"
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param stp 多用户StpLogic
     * @param authUser 登录用户信息
     * @param saLoginModel     配置参数
     */
    fun <T> login(stp: StpLogic, authUser: AuthUser<T>, saLoginModel: SaLoginModel) {
        // 缓存数据到上下文持有类
        val storage = SaHolder.getStorage()
        storage[LOGIN_USER_KEY] = authUser
        val model = ObjectUtil.defaultIfNull(saLoginModel, SaLoginModel())
        //satoken真实登录方法
        stp.login(
            authUser.getAuthLabel(),
            model
                .setExtra(USER_KEY, authUser.userId)
        )
        // 缓存到会话对象
        stp.tokenSession[LOGIN_USER_KEY] = authUser
    }

    fun logout(stp: StpLogic) {
        if (isLogin(stp)) stp.logout()
    }

    fun logout(stp: StpLogic, device: String) {
        if (isLogin(stp)) stp.logout(stp.loginId, device)
    }

    fun kickout(stp: StpLogic, loginId: String) {
        stp.kickout(loginId)
    }

    fun kickout(stp: StpLogic, loginId: String, device: String) {
        stp.kickout(loginId, device)
    }

    private fun getStoreUser(stp: StpLogic): Any? {
        return getStorageIfAbsentSet(
            LOGIN_USER_KEY,
            Supplier getStorageIfAbsentSet@{
                val session = stp.tokenSession
                if (ObjectUtil.isNull(session)) {
                    return@getStorageIfAbsentSet null
                }
                session[LOGIN_USER_KEY]
            })
    }

    private fun getStoreUser(stp: StpLogic, loginId: String): Any? {
        return getStorageIfAbsentSet(
            LOGIN_USER_KEY,
            Supplier getStorageIfAbsentSet@{
                val session = stp.getSessionByLoginId(loginId)
                if (ObjectUtil.isNull(session)) {
                    return@getStorageIfAbsentSet null
                }
                session[LOGIN_USER_KEY]
            })
    }

    fun <T> getTypedAuthUser(stp: StpLogic): AuthUser<T> {
        val getObj = getStoreUser(stp) ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
        return getObj as AuthUser<T>
    }

    fun <T> getTypedAuthUser(stp: StpLogic, loginId: String): AuthUser<T> {
        val getObj =
            getStoreUser(stp, loginId) ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
        return getObj as AuthUser<T>
    }


    /**
     * 获取用户(多级缓存)
     */
    fun getAuthUser(stp: StpLogic): AuthUser<*> {
        val getObj = getStoreUser(stp) ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
        return getObj as AuthUser<*>
    }

    fun getAuthUser(stp: StpLogic, loginId: String): AuthUser<*> {
        val getObj =
            getStoreUser(stp, loginId) ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
        return getObj as AuthUser<*>
    }

    fun getToken(stp: StpLogic): String {
        return stp.tokenValue ?: throw NotLoginException(NotLoginException.NOT_TOKEN_MESSAGE, null, null)
    }

    fun getUserType(stp: StpLogic): UserType {
        val loginUser: AuthUser<*> = getAuthUser(stp)
        return UserType.getEnumByCode(loginUser.userType)
    }

    fun getUserId(stp: StpLogic): String {
        val loginUser: AuthUser<*> = getAuthUser(stp)
        return loginUser.userId
    }

    fun getExtra(stp: StpLogic, key: String): Any? {
        return getStorageIfAbsentSet(key) {
            stp.getExtra(
                key
            )
        }
    }

    fun getUsername(stp: StpLogic): String? {
        val loginUser: AuthUser<*> = getAuthUser(stp)
        return loginUser.username
    }

    fun isLogin(stp: StpLogic): Boolean {
        return stp.isLogin
    }

    fun isLogin(stp: StpLogic, loginId: Any): Boolean {
        return stp.isLogin(loginId)
    }

    fun <T> refreshUser(stp: StpLogic, data: AuthUser<T>) {
        if (isLogin(stp)) {
            SaHolder.getStorage().set(LOGIN_USER_KEY, data)
            StpUtil.getTokenSession()[LOGIN_USER_KEY] = data
        }
    }

    fun <T> refreshUser(stp: StpLogic, loginId: Any, data: AuthUser<T>) {
        val myLoginId = stp.loginId
        if (myLoginId == loginId) {
            SaHolder.getStorage().set(LOGIN_USER_KEY, data)
            StpUtil.getTokenSession()[LOGIN_USER_KEY] = data
        }
        if (isLogin(stp, loginId)) {
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
