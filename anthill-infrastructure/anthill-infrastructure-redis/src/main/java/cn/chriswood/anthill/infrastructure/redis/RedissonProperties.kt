package cn.chriswood.anthill.infrastructure.redis

import org.redisson.config.ReadMode
import org.redisson.config.SubscriptionMode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("redisson")
data class RedissonProperties(
    /**
     * redis缓存key前缀
     */
    var keyPrefix: String?,
    /**
     * 线程池数量,默认值 = 当前处理核数量 * 2
     */
    var threads: Int = Runtime.getRuntime().availableProcessors() * 2,
    /**
     * 线程池数量,默认值 = 当前处理核数量 * 2
     */
    val nettyThreads: Int = Runtime.getRuntime().availableProcessors() * 2,
    /**
     * 单机服务配置
     */
    @NestedConfigurationProperty
    var singleServerConfig: SingleServerConfig? = null,
    @NestedConfigurationProperty
    var clusterServersConfig: ClusterServersConfig? = null,
) {

    class SingleServerConfig {
        /**
         * 客户端名称
         */
        val clientName: String? = null

        /**
         * 最小空闲连接数
         */
        val connectionMinimumIdleSize = 0

        /**
         * 连接池大小
         */
        val connectionPoolSize = 0

        /**
         * 连接空闲超时，单位：毫秒
         */
        val idleConnectionTimeout = 0

        /**
         * 命令等待超时，单位：毫秒
         */
        val timeout = 0

        /**
         * 发布和订阅连接池大小
         */
        val subscriptionConnectionPoolSize = 0
    }

    class ClusterServersConfig {
        /**
         * 客户端名称
         */
        val clientName: String? = null

        /**
         * master最小空闲连接数
         */
        val masterConnectionMinimumIdleSize = 0

        /**
         * master连接池大小
         */
        val masterConnectionPoolSize = 0

        /**
         * slave最小空闲连接数
         */
        val slaveConnectionMinimumIdleSize = 0

        /**
         * slave连接池大小
         */
        val slaveConnectionPoolSize = 0

        /**
         * 连接空闲超时，单位：毫秒
         */
        val idleConnectionTimeout = 0

        /**
         * 命令等待超时，单位：毫秒
         */
        val timeout = 0

        /**
         * 发布和订阅连接池大小
         */
        val subscriptionConnectionPoolSize = 0

        /**
         * 读取模式
         */
        val readMode: ReadMode? = null

        /**
         * 订阅模式
         */
        val subscriptionMode: SubscriptionMode? = null
    }

}
