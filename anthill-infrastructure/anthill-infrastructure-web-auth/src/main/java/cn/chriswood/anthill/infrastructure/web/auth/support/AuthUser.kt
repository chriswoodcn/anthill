package cn.chriswood.anthill.infrastructure.web.auth.support

interface AuthUser<T> {
    /**
     * 用户类型
     */
    var userType: String

    /**
     * 用户ID
     */
    var userId: String

    /**
     * 菜单权限
     */
    var permissions: Set<String>

    /**
     * 角色权限
     */
    var roles: Set<String>

    /**
     * 用户名
     */
    var username: String?

    /**
     * 用户数据体
     */
    var instance: T

    fun getAuthLabel(): String {
        return "$userType:$userId"
    }
}
