package cn.chriswood.anthill.infrastructure.redis

import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import cn.chriswood.anthill.infrastructure.redis.support.KeyPrefixHandler
import cn.chriswood.anthill.infrastructure.redis.support.RedissonProperties
import cn.chriswood.anthill.infrastructure.redis.support.SpringCacheManager
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import org.redisson.client.codec.StringCodec
import org.redisson.codec.CompositeCodec
import org.redisson.codec.TypedJsonJacksonCodec
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(RedissonProperties::class)
@EnableCaching
@ConditionalOnProperty(
    prefix = "anthill.redisson",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class RedisConfig(
    private val redissonProperties: RedissonProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun redissonCustomizer(): RedissonAutoConfigurationCustomizer {
        return RedissonAutoConfigurationCustomizer { config ->

            val om: ObjectMapper = JacksonUtil.objectMapper.copy()
            om.setVisibility(
                PropertyAccessor.ALL,
                JsonAutoDetect.Visibility.ANY
            )
            // 指定序列化输入的类型，类必须是非final修饰的。序列化时将对象全类名一起保存下来
            om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
            )
            val jsonCodec = TypedJsonJacksonCodec(Any::class.java, om)
            // 组合序列化 key 使用 String 内容使用通用 json 格式
            val codec = CompositeCodec(StringCodec.INSTANCE, jsonCodec, jsonCodec)
            config.setThreads(redissonProperties.threads)
                .setNettyThreads(redissonProperties.nettyThreads) // 缓存 Lua 脚本 减少网络传输(redisson 大部分的功能都是基于 Lua 脚本实现)
                .setUseScriptCache(true)
                .setCodec(codec)
            val singleServerConfig: RedissonProperties.SingleServerConfig? = redissonProperties.singleServerConfig
            if (null != singleServerConfig) {
                // 使用单机模式
                config.useSingleServer() //设置redis key前缀
                    .setNameMapper(KeyPrefixHandler(redissonProperties.keyPrefix))
                    .setTimeout(singleServerConfig.timeout)
                    .setClientName(singleServerConfig.clientName)
                    .setIdleConnectionTimeout(singleServerConfig.idleConnectionTimeout)
                    .setSubscriptionConnectionPoolSize(singleServerConfig.subscriptionConnectionPoolSize)
                    .setConnectionMinimumIdleSize(singleServerConfig.connectionMinimumIdleSize)
                    .setConnectionPoolSize(singleServerConfig.connectionPoolSize)
            }
            // 集群配置方式 参考下方注释
            val clusterServersConfig: RedissonProperties.ClusterServersConfig? =
                redissonProperties.clusterServersConfig
            if (null != clusterServersConfig) {
                config.useClusterServers() //设置redis key前缀
                    .setNameMapper(KeyPrefixHandler(redissonProperties.keyPrefix))
                    .setTimeout(clusterServersConfig.timeout)
                    .setClientName(clusterServersConfig.clientName)
                    .setIdleConnectionTimeout(clusterServersConfig.idleConnectionTimeout)
                    .setSubscriptionConnectionPoolSize(clusterServersConfig.subscriptionConnectionPoolSize)
                    .setMasterConnectionMinimumIdleSize(clusterServersConfig.masterConnectionMinimumIdleSize)
                    .setMasterConnectionPoolSize(clusterServersConfig.masterConnectionPoolSize)
                    .setSlaveConnectionMinimumIdleSize(clusterServersConfig.slaveConnectionMinimumIdleSize)
                    .setSlaveConnectionPoolSize(clusterServersConfig.slaveConnectionPoolSize)
                    .setReadMode(clusterServersConfig.readMode)
                    .setSubscriptionMode(clusterServersConfig.subscriptionMode)
            }
            log.debug(">>>>>>>>>> init RedisConfig redissonCustomizer >>>>>>>>>>")
        }
    }

    @Bean
    fun cacheManager(): CacheManager {
        val springCacheManager = SpringCacheManager()
        log.debug(">>>>>>>>>> init RedisConfig CacheManager >>>>>>>>>>")
        return springCacheManager
    }
}
