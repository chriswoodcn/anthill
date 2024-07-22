package com.taotao.bmm.support

import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.IdUtil
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

/**
 * 自定义ID生成器
 */
class SnowflakeIDGenerator : IdentifierGenerator {
    /**
     * 根据32位的IP地址生成workId和dataId  25-32位为workId  17-24位为dataId
     */
    override fun generate(p0: SharedSessionContractImplementor?, p1: Any?): Any {
        var workId = 0L
        var dataId = 0L
        val filtered = NetUtil.localIpv4s().filter { NetUtil.LOCAL_IP != it }
        if (filtered.isNotEmpty()) {
            val split = filtered[0].split(".")
            workId = split[3].toLong()
            dataId = split[2].toLong()
        }
        val snowflake = IdUtil.getSnowflake(workId, dataId)
        return snowflake.nextId()
    }
}