package cn.chriswood.anthill.mongo.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "anthill.mongo")
data class MongoConfigProperties(
    val enabled: Boolean = false,
    val type: String = MongoDbTypeEnum.SINGLE.code,
    val transaction: Transaction,
    val dbs: Map<String, DbInfo>?,
) {
    class Transaction {
        val enabled: Boolean = false
    }

    class DbInfo {
        val uri: String? = null
    }
}
