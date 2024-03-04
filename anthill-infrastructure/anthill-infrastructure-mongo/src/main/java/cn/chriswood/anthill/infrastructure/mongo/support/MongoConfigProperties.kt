package cn.chriswood.anthill.infrastructure.mongo.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.mongo")
data class MongoConfigProperties(
    /**
     * 开关
     */
    val enabled: Boolean = false,
    /**
     * 类型 预留动态和多数据源 暂时只有简单配置 single
     */
    val type: String = MongoDbTypeEnum.SINGLE.code,
    /**
     * 是否开启事务 注意：开启事务需要分片集mongodb
     */
    val transaction: Transaction = Transaction(false),
    /**
     * 数据库uri配置
     */
    val dbs: Map<String, DbInfo>?,
) {
    class Transaction(val enabled: Boolean = false)

    class DbInfo(val uri: String? = null)
}
