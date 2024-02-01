package cn.chriswood.anthill.infrastructure.webauth.handler

data class AuthUser(

    /**
     * 用户类型
     */
    var userType: String,

    /**
     * 设备类型
     */
    val deviceType: String,

    /**
     * 终端类型
     */
    val endpointType: String,

    /**
     * 用户ID
     */
    var userId: Long = 0L,

    /**
     * 部门ID
     */
    var deptId: Long? = null,

    /**
     * 部门名
     */
    var deptName: String? = null,

    /**
     * 用户唯一标识
     */
    var token: String,

    /**
     * 登录时间
     */
    var loginTime: Long = 0L,

    /**
     * 过期时间
     */
    var expireTime: Long = 0L,

    /**
     * 登录IP地址
     */
    var ipaddr: String? = null,

    /**
     * 登录地点
     */
    var loginLocation: String? = null,

    /**
     * 浏览器类型
     */
    var browser: String? = null,

    /**
     * 操作系统
     */
    var os: String? = null,

    /**
     * 菜单权限
     */
    var menuPermission: Set<String> = setOf(),

    /**
     * 角色权限
     */
    var rolePermission: Set<String> = setOf(),

    /**
     * 用户名
     */
    var username: String? = null,

    /**
     * 用户昵称
     */

    var nickname: String? = null,

    /**
     * 角色对象
     */
    var roles: List<Any> = listOf(),

    /**
     * 数据权限 当前角色ID
     */
    var roleId: Long = 0L,
) {
    fun getAuthLabel(): String {
        return "$userType:$userId"
    }
}
