package cn.chriswood.anthill.infrastructure.core.enums

/**
 * 设备类型
 */
enum class DeviceType(var code: String) {
    /**
     * pc端
     */
    PC("pc"),

    /**
     * 手机端
     */
    PHONE("phone"),

    /**
     * pad端
     */
    PAD("pad"),

    /**
     * iot端
     */
    IOT("iot"),

    /**
     * 未知
     */
    UNKNOWN("unknown");

    companion object {
        fun getEnumByCode(code: String): DeviceType {
            var enum = UNKNOWN
            DeviceType.entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
