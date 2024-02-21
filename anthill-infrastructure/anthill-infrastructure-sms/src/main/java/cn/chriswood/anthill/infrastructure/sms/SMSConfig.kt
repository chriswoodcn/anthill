package cn.chriswood.anthill.infrastructure.sms

import cn.chriswood.anthill.infrastructure.redis.RedisUtil
import org.dromara.sms4j.api.dao.SmsDao
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "anthill.sms",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SMSConfig {
    @Bean
    @ConditionalOnClass(RedisUtil::class)
    fun smsDaoImpl(): SmsDao {
        return SmsDaoImpl()
    }
}
