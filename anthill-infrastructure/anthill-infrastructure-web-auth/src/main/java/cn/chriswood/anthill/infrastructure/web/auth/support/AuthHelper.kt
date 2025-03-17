package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.core.enums.UserType
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpLogic
import cn.hutool.core.util.ObjectUtil
import java.io.Serializable

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
        val model = ObjectUtil.defaultIfNull(saLoginModel, SaLoginModel())
        //satoken真实登录方法
        stp.login(
            authUser.getAuthLabel(),
            model
                .setExtra(USER_KEY, authUser.userId)
        )
        // 缓存到会话对象 在 SaSession 存储的数据在一次会话范围内有效，会话结束后数据自动清除。必须登录后才能使用 SaSession 对象。
        stp.session[LOGIN_USER_KEY] = authUser
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
        return getStorageIfAbsentSet(stp)
    }

    private fun getStoreUser(stp: StpLogic, loginId: String): Any? {
        return getStorageIfAbsentSet(stp, loginId)
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
        return getStorageIfAbsentSet(stp) {
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
            stp.session[LOGIN_USER_KEY] = data
        }
    }

    fun <T> refreshUser(stp: StpLogic, loginId: Any, data: AuthUser<T>) {
        val myLoginId = stp.loginId
        if (myLoginId == loginId) {
            stp.session[LOGIN_USER_KEY] = data
        }
        if (isLogin(stp, loginId)) {
            val session = stp.getSessionByLoginId(loginId)
            session[LOGIN_USER_KEY] = data
        }
    }

    private fun getStorageIfAbsentSet(stp: StpLogic): Any? {
        return try {
            stp.session[LOGIN_USER_KEY]
        } catch (e: Exception) {
            null
        }
    }

    private fun getStorageIfAbsentSet(stp: StpLogic, loginId: Any): Any? {
        return try {
            stp.getSessionByLoginId(loginId)[LOGIN_USER_KEY]
        } catch (e: Exception) {
            null
        }
    }
}
