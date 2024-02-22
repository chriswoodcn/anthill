package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.util.SaFoxUtil
import java.time.Duration

class SaTokenDaoImpl : SaTokenDao {
    override fun get(key: String): String? {
        return RedisUtil.getCacheObject(key)
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
    }

    override fun update(key: String, value: String?) {
        if (RedisUtil.hasKey(key)) {
            RedisUtil.setCacheObjectSaveTtl(key, value, true)
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

    override fun getObject(key: String): Any {
        return RedisUtil.getCacheObject(key)
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
    }

    override fun updateObject(key: String, value: Any) {
        if (RedisUtil.hasKey(key)) {
            RedisUtil.setCacheObjectSaveTtl(key, value, true)
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
        val keys: Collection<String> = RedisUtil.keys("$prefix*$keyword*")
        val list: List<String> = ArrayList(keys)
        return SaFoxUtil.searchList(list, start, size, sortType)
    }
}
