package cn.chriswood.anthill.infrastructure.core.enums

/**
 * 用户类型
 */
enum class UserType(var code: String) {
    /**
     * 系统用户
     */
    SYS_USER("sys_user"),

    /**
     * 普通用户
     */
    APP_USER("app_user"),

    /**
     * 未知类型
     */
    UNKNOWN("unknown");

    companion object {
        fun getEnumByCode(code: String): UserType {
            var enum = UNKNOWN
            entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
