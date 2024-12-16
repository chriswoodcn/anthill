import cn.hutool.cache.CacheUtil
import cn.hutool.core.date.DateUnit
import cn.hutool.core.net.NetUtil
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.test.Test


class TestMain {
    @Test
    fun testUTC2GMT() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        println(now)
        val toLocalDateTime = now.toLocalDateTime()
        println(toLocalDateTime)
    }

    @Test
    fun testLocalhost() {
        println("本机IP地址为：${NetUtil.getLocalhostStr()}")
        println("本机IP地址为：${NetUtil.localIpv4s()}")
        println("本机IP地址为：${NetUtil.localIpv6s()}")
        println("本机MAC地址为：${NetUtil.getLocalMacAddress()}")
        println("本机Host为：${NetUtil.getLocalHostName()}")
    }

    @Test
    fun testTimedCache() {
        val timedCache = CacheUtil.newTimedCache<Any, Any>(0)
        timedCache.schedulePrune(100)
        timedCache.setListener { k, v ->
            println("removed key: ${k as String}, value: ${v as String}")
        }
        println(">>>>>>>>>> timedCache has set Listener")
        timedCache.put("aaa", "cccccc", DateUnit.SECOND.millis * 1)
        val cacheObjIterator = timedCache.cacheObjIterator()
        println(">>>>>>>>>> timedCache has put aaa")
        cacheObjIterator.forEach {
            println("${it.key}-${it.value}")
            println(it.expiredTime)
            var get = timedCache.get(it.key)
        }
        timedCache.asIterable()
        Thread.sleep(3000)
        println(">>>>>>>>>> finish")
    }
}
