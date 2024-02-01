package cn.chriswood.anthill.infrastructure.core.enums

/**
 * 终端类型
 */
enum class EndpointType(var code: String) {
    /**
     * 浏览器
     */
    BROWSER("browser"),

    /**
     * 应用程序
     */
    APP("app"),

    /**
     * 小程序
     */
    XCX("xcx"),

    /**
     * 未知
     */
    UNKNOWN("unknown");

    companion object {
        fun getEnumByCode(code: String): EndpointType {
            var enum = UNKNOWN
            EndpointType.entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
