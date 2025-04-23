package cn.chriswood.anthill.infrastructure.mybatisflex.support

import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.IdUtil
import com.mybatisflex.core.keygen.IKeyGenerator

class GenKeySnowflakeGenerator : IKeyGenerator {
    override fun generate(entity: Any?, keyColumn: String?): Any {
        var workId = 0L
        val filtered = NetUtil.localIpv4s()
            .filter { NetUtil.LOCAL_IP != it }
        if (filtered.isNotEmpty()) {
            val split = filtered[0].split(".")
            workId = split[3].toLong()
        }
        val snowflake = IdUtil.getSnowflake(workId % 32)
        return snowflake.nextIdStr()
    }
}
