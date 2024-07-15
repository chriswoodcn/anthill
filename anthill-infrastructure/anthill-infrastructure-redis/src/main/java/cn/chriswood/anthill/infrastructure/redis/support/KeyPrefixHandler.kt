package cn.chriswood.anthill.infrastructure.redis.support

import org.redisson.api.NameMapper

class KeyPrefixHandler(private val keyPrefix: String?) : NameMapper {
    private val delegateKeyPrefix: String
        get() {
            return if (keyPrefix.isNullOrBlank()) "app:" else "$keyPrefix:"
        }

    /**
     * 增加前缀
     */
    override fun map(name: String?): String? {
        if (name.isNullOrBlank()) {
            return null
        }
        return if (!name.startsWith(delegateKeyPrefix)) {
            delegateKeyPrefix + name
        } else name
    }

    /**
     * 去除前缀
     */
    override fun unmap(name: String?): String? {
        if (name.isNullOrBlank()) {
            return null
        }
        return if (name.startsWith(delegateKeyPrefix)) {
            name.substring(delegateKeyPrefix.length)
        } else name
    }
}
