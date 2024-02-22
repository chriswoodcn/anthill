package cn.chriswood.anthill.infrastructure.web.auth.handler

interface AuthUser {
    /**
     * 用户类型
     */
    var userType: String

    /**
     * 设备类型
     */
    val deviceType: String

    /**
     * 终端类型
     */
    val endpointType: String

    /**
     * 用户ID
     */
    var userId: Long

    /**
     * 部门ID
     */
    var deptId: Long?

    /**
     * 部门名
     */
    var deptName: String?

    /**
     * 用户唯一标识
     */
    var token: String

    /**
     * 登录时间
     */
    var loginTime: Long

    /**
     * 过期时间
     */
    var expireTime: Long

    /**
     * 登录IP地址
     */
    var ipaddr: String?

    /**
     * 登录地点
     */
    var loginLocation: String?

    /**
     * 浏览器类型
     */
    var browser: String?

    /**
     * 操作系统
     */
    var os: String?

    /**
     * 菜单权限
     */
    var menuPermission: Set<String>

    /**
     * 角色权限
     */
    var rolePermission: Set<String>

    /**
     * 用户名
     */
    var username: String?

    /**
     * 用户昵称
     */
    var nickname: String?

    /**
     * 角色对象
     */
    var roles: List<Any>

    /**
     * 数据权限 当前角色ID
     */
    var roleId: Long

    fun getAuthLabel(): String {
        return "$userType:$userId"
    }
}
