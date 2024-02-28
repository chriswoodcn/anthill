package cn.chriswood.anthill.infrastructure.redis.utils

import cn.hutool.extra.spring.SpringUtil
import org.redisson.api.RMap
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

object RedisCacheUtil {
    private val MANAGER: CacheManager = SpringUtil.getBean(CacheManager::class.java)

    fun keys(cacheNames: String): Set<Any> {
        val cache = MANAGER.getCache(cacheNames) ?: return emptySet()
        val rMap = cache.nativeCache as RMap<*, *>
        return rMap.keys
    }

    operator fun <T> get(cacheNames: String, key: Any): T? {
        val cache = MANAGER.getCache(cacheNames) ?: return null
        val wrapper: Cache.ValueWrapper = cache.get(key) ?: return null
        return wrapper.get() as T?
    }

    fun put(cacheNames: String, key: Any, value: Any?) {
        val cache = MANAGER.getCache(cacheNames) ?: return
        cache.put(key, value)
    }

    fun evict(cacheNames: String, key: Any) {
        val cache = MANAGER.getCache(cacheNames) ?: return
        cache.evict(key)
    }

    fun clear(cacheNames: String) {
        val cache = MANAGER.getCache(cacheNames) ?: return
        cache.clear()
    }
}
