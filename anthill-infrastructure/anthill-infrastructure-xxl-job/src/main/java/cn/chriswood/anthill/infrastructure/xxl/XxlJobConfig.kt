package cn.chriswood.anthill.infrastructure.xxl

import cn.chriswood.anthill.infrastructure.xxl.support.XxlJobProperties
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(value = ["anthill.xxl-job.enabled"], havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties::class)
class XxlJobConfig(
    private val xxlJobProperties: XxlJobProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun xxlJobExecutor(): XxlJobSpringExecutor {
        log.info(">>>>>>>>>>> xxl-job config init >>>>>>>>>>>")
        val xxlJobSpringExecutor = XxlJobSpringExecutor().apply {
            this.setAdminAddresses(xxlJobProperties.adminAddresses)
            this.setAccessToken(xxlJobProperties.accessToken)
            this.setAppname(xxlJobProperties.executor?.appname)
            this.setAddress(xxlJobProperties.executor?.address)
            this.setIp(xxlJobProperties.executor?.ip)
            this.setPort(xxlJobProperties.executor?.port ?: 9101)
            this.setLogPath(xxlJobProperties.executor?.logPath)
            this.setLogRetentionDays(xxlJobProperties.executor?.logRetentionDays ?: 3)
        }
        return xxlJobSpringExecutor
    }

}
