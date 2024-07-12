package cn.chriswood.anthill.infrastructure.redis.support

import org.redisson.api.NameMapper

class KeyPrefixHandler(keyPrefix: String?) : NameMapper {
    private var keyPrefix: String? = null
        get() {
            return if (field.isNullOrBlank()) "app:" else "$field:"
        }

    /**
     * 增加前缀
     */
    override fun map(name: String?): String? {
        if (name.isNullOrBlank()) {
            return null
        }
        return if (keyPrefix.isNullOrBlank() && !name.startsWith(keyPrefix!!)) {
            keyPrefix + name
        } else name
    }

    /**
     * 去除前缀
     */
    override fun unmap(name: String?): String? {
        if (name.isNullOrBlank()) {
            return null
        }
        return if (keyPrefix.isNullOrBlank() && name.startsWith(keyPrefix!!)) {
            name.substring(keyPrefix!!.length)
        } else name
    }
}
