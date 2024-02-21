package cn.chriswood.anthill.infrastructure.sms

import cn.chriswood.anthill.infrastructure.redis.RedisUtil
import org.dromara.sms4j.api.dao.SmsDao
import java.time.Duration

class SmsDaoImpl : SmsDao {
    private val KEY_PREFIX = "SMS:"

    private fun getRealKey(key: String): String {
        return KEY_PREFIX + key
    }

    override fun set(key: String?, value: Any?, cacheTime: Long) {
        if (key == null) return
        RedisUtil.setCacheObjectTtl(getRealKey(key), value, Duration.ofSeconds(cacheTime))
    }

    override fun set(key: String?, value: Any?) {
        if (key == null) return
        RedisUtil.setCacheObject(getRealKey(key), value)
    }

    override fun get(key: String?): Any? {
        if (key == null) return null
        return RedisUtil.getCacheObject(key)
    }

    override fun remove(key: String?): Any? {
        if (key == null) return null
        val cacheObject = RedisUtil.getCacheObject<Any?>(key)
        RedisUtil.deleteObject(key)
        return cacheObject
    }

    override fun clean() {
        RedisUtil.deleteKeys(KEY_PREFIX)
    }
}
