package cn.chriswood.anthill.infrastructure.core.enums

/**
 * 用户类型
 */
enum class UserType(var code: String) {
    /**
     * 系统用户
     */
    SYS_USER("SYS_USER"),

    /**
     * 普通用户
     */
    APP_USER("APP_USER"),

    /**
     * 未知类型
     */
    UNKNOWN("UNKNOWN");

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
