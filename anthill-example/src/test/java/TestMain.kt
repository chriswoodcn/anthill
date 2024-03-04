
import cn.hutool.core.net.NetUtil
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.test.Test


class TestMain {
    @Test
    fun testUTC2GMT(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        println(now)
        val toLocalDateTime = now.toLocalDateTime()
        println(toLocalDateTime)
    }
    @Test
    fun testLocalhost(){
        println("本机IP地址为：${NetUtil.getLocalhostStr()}")
        println("本机IP地址为：${NetUtil.localIpv4s()}")
        println("本机IP地址为：${NetUtil.localIpv6s()}")
        println("本机MAC地址为：${NetUtil.getLocalMacAddress()}")
        println("本机Host为：${NetUtil.getLocalHostName()}")
    }
}
