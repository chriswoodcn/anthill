package cn.chriswood.anthill.infrastructure.jpa.support

import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.IdUtil
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

class GenKeySnowflakeGenerator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Any {
        var workId = 0L
        val filtered = NetUtil.localIpv4s()
            .filter { NetUtil.LOCAL_IP != it }
        if (filtered.isNotEmpty()) {
            val split = filtered[0].split(".")
            workId = split[3].toLong()
        }
        val snowflake = IdUtil.getSnowflake(workId)
        return snowflake.nextId()
    }
}
