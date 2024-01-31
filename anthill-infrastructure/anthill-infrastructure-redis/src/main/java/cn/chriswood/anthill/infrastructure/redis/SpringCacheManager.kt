package cn.chriswood.anthill.infrastructure.redis

import org.redisson.api.RMap
import org.redisson.api.RMapCache
import org.redisson.spring.cache.CacheConfig
import org.redisson.spring.cache.RedissonCache
import org.springframework.boot.convert.DurationStyle
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.transaction.TransactionAwareCacheDecorator
import org.springframework.util.StringUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * 修改 RedissonSpringCacheManager 源码 重写 cacheName 处理方法 支持多参数
 */
class SpringCacheManager : CacheManager {

    var dynamic = true

    var allowNullValues = true

    var transactionAware = true

    private var configMap: Map<String, CacheConfig> = ConcurrentHashMap()

    var instanceMap: ConcurrentMap<String, Cache> = ConcurrentHashMap()

    fun setConfigMap(config: Map<String, CacheConfig>) {
        configMap = config
    }

    private fun createDefaultConfig(): CacheConfig {
        return CacheConfig()
    }

    override fun getCache(name: String): Cache? {
        val mainName: String
        // 重写 cacheName 支持多参数
        val array = StringUtils.delimitedListToStringArray(name, "#")
        mainName = array[0]

        val cache = instanceMap[mainName]
        if (cache != null) {
            return cache
        }
        if (!dynamic) {
            return cache
        }

        var config = configMap[mainName]
        if (config == null) {
            config = createDefaultConfig()
            configMap.plus(mainName to config)
        }

        if (array.size > 1) {
            config.ttl = DurationStyle.detectAndParse(array[1]).toMillis()
        }
        if (array.size > 2) {
            config.maxIdleTime = DurationStyle.detectAndParse(array[2]).toMillis()
        }
        if (array.size > 3) {
            config.maxSize = array[3].toInt()
        }

        return if (config.maxIdleTime == 0L && config.ttl == 0L && config.maxSize == 0) {
            createMap(mainName, config)
        } else createMapCache(mainName, config)

    }

    private fun createMap(name: String, config: CacheConfig): Cache {
        val map: RMap<Any, Any> = RedisUtil.getClient().getMap(name)
        var cache: Cache = RedissonCache(map, allowNullValues)
        if (transactionAware) {
            cache = TransactionAwareCacheDecorator(cache)
        }
        val oldCache = instanceMap.putIfAbsent(name, cache)
        if (oldCache != null) {
            cache = oldCache
        }
        return cache
    }

    private fun createMapCache(name: String, config: CacheConfig): Cache {
        val map: RMapCache<Any, Any> = RedisUtil.getClient().getMapCache(name)
        var cache: Cache = RedissonCache(map, config, allowNullValues)
        if (transactionAware) {
            cache = TransactionAwareCacheDecorator(cache)
        }
        val oldCache = instanceMap.putIfAbsent(name, cache)
        if (oldCache != null) {
            cache = oldCache
        } else {
            map.setMaxSize(config.maxSize)
        }
        return cache
    }

    override fun getCacheNames(): MutableCollection<String> {
        return Collections.unmodifiableSet(configMap.keys)
    }
}
