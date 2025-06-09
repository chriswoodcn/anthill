package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.util.SaFoxUtil
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration
import java.util.concurrent.TimeUnit


class SaTokenDaoImpl : SaTokenDao {

    var CAFFEINE: Cache<String, Any> = Caffeine.newBuilder() // 设置最后一次写入或访问后经过固定时间过期
        .expireAfterWrite(5, TimeUnit.SECONDS) // 初始的缓存空间大小
        .initialCapacity(100) // 缓存的最大条数
        .maximumSize(1000)
        .build()

    override fun get(key: String): String? {
        return CAFFEINE.get(key) {
            RedisUtil.getCacheObject(it)
        } as String?
    }

    override fun set(key: String, value: String, timeout: Long) {
        if (timeout == 0L || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            RedisUtil.setCacheObject(key, value)
        } else {
            RedisUtil.setCacheObjectTtl(key, value, Duration.ofSeconds(timeout))
        }
        CAFFEINE.invalidate(key)
    }

    override fun update(key: String, value: String?) {
        if (RedisUtil.hasKey(key)) {
            RedisUtil.setCacheObjectSaveTtl(key, value, true)
            CAFFEINE.invalidate(key);
        }
    }

    override fun delete(key: String) {
        RedisUtil.deleteObject(key)
    }

    override fun getTimeout(key: String): Long {
        val timeout = RedisUtil.getTimeToLive<Any>(key)
        return if (timeout < 0) timeout else timeout / 1000
    }

    override fun updateTimeout(key: String, timeout: Long) {
        RedisUtil.expireDuration(key, Duration.ofSeconds(timeout))
    }

    override fun getObject(key: String): Any? {
        return CAFFEINE.get(key) { RedisUtil.getCacheObject(it) }
    }

    override fun setObject(key: String, value: Any, timeout: Long) {
        if (timeout == 0L || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            RedisUtil.setCacheObject(key, value)
        } else {
            RedisUtil.setCacheObjectTtl(key, value, Duration.ofSeconds(timeout))
        }
        CAFFEINE.invalidate(key)
    }

    override fun updateObject(key: String, value: Any) {
        if (RedisUtil.hasKey(key)) {
            RedisUtil.setCacheObjectSaveTtl(key, value, true)
            CAFFEINE.invalidate(key)
        }
    }

    override fun deleteObject(key: String) {
        RedisUtil.deleteObject(key)
    }

    override fun getObjectTimeout(key: String): Long {
        val timeout = RedisUtil.getTimeToLive<Any>(key)
        return if (timeout < 0) timeout else timeout / 1000
    }

    override fun updateObjectTimeout(key: String, timeout: Long) {
        RedisUtil.expireDuration(key, Duration.ofSeconds(timeout))
    }

    override fun searchData(
        prefix: String,
        keyword: String,
        start: Int,
        size: Int,
        sortType: Boolean
    ): MutableList<String> {
        val keyStr = "$prefix*$keyword*"
        val list: List<String> = CAFFEINE.get(keyStr) {
            val keys: Collection<String> = RedisUtil.keys(keyStr)
            val list: List<String> = ArrayList(keys)
            SaFoxUtil.searchList(list, start, size, sortType)
        } as List<String>
        return list.toMutableList()
    }
}
