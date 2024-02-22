package cn.chriswood.anthill.infrastructure.redis.support

import org.redisson.config.ReadMode
import org.redisson.config.SubscriptionMode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("anthill.redisson")
data class RedissonProperties(
    /**
     * 是否开启
     */
    var enabled: Boolean?,
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
    var nettyThreads: Int = Runtime.getRuntime().availableProcessors() * 2,
    /**
     * 单机服务配置
     */
    @NestedConfigurationProperty
    var singleServerConfig: SingleServerConfig? = null,
    @NestedConfigurationProperty
    var clusterServersConfig: ClusterServersConfig? = null,
) {

    data class SingleServerConfig(
        /**
         * 客户端名称
         */
        val clientName: String?,

        /**
         * 最小空闲连接数
         */
        val connectionMinimumIdleSize: Int = 0,

        /**
         * 连接池大小
         */
        val connectionPoolSize: Int = 0,

        /**
         * 连接空闲超时，单位：毫秒
         */
        val idleConnectionTimeout: Int = 0,

        /**
         * 命令等待超时，单位：毫秒
         */
        val timeout: Int = 0,

        /**
         * 发布和订阅连接池大小
         */
        val subscriptionConnectionPoolSize: Int = 0,
    )

    data class ClusterServersConfig(
        /**
         * 客户端名称
         */
        val clientName: String? = null,

        /**
         * master最小空闲连接数
         */
        val masterConnectionMinimumIdleSize: Int = 0,

        /**
         * master连接池大小
         */
        val masterConnectionPoolSize: Int = 0,

        /**
         * slave最小空闲连接数
         */
        val slaveConnectionMinimumIdleSize: Int = 0,

        /**
         * slave连接池大小
         */
        val slaveConnectionPoolSize: Int = 0,

        /**
         * 连接空闲超时，单位：毫秒
         */
        val idleConnectionTimeout: Int = 0,

        /**
         * 命令等待超时，单位：毫秒
         */
        val timeout: Int = 0,

        /**
         * 发布和订阅连接池大小
         */
        val subscriptionConnectionPoolSize: Int = 0,

        /**
         * 读取模式
         */
        val readMode: ReadMode? = null,

        /**
         * 订阅模式
         */
        val subscriptionMode: SubscriptionMode? = null,
    )

}
