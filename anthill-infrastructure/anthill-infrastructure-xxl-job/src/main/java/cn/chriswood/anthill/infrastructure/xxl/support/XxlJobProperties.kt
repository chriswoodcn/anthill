package cn.chriswood.anthill.infrastructure.xxl.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("anthill.xxl-job")
class XxlJobProperties {
    var enabled: Boolean? = null

    var adminAddresses: String? = null

    var accessToken: String? = null

    var executor: Executor? = null

    class Executor {
        var appname: String? = null
        var address: String? = null
        var ip: String? = null
        var port = 0
        var logPath: String? = null
        var logRetentionDays = 0
    }
}
