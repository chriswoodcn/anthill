package cn.chriswood.anthill.infrastructure.mybatisflex

import cn.chriswood.anthill.infrastructure.mybatisflex.support.*
import com.mybatisflex.core.FlexGlobalConfig
import com.mybatisflex.core.audit.AuditManager
import com.mybatisflex.core.audit.AuditMessage
import com.mybatisflex.core.keygen.KeyGeneratorFactory
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration


@AutoConfiguration
class MybatisflexConfig : MyBatisFlexCustomizer {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun customize(config: FlexGlobalConfig) {
        KeyGeneratorFactory.register(
            KeyGenerators.anthillUUID, GenKeyUUIDGenerator()
        )
        KeyGeneratorFactory.register(
            KeyGenerators.anthillObjectId, GenKeyObjectGenerator()
        )
        KeyGeneratorFactory.register(
            KeyGenerators.anthillSnowFlakeId, GenKeySnowflakeGenerator()
        )
        // 全局配置insert监听
        config.registerInsertListener(FlexInsertListener())
        // 全局配置update监听
        config.registerUpdateListener(FlexUpdateListener())
        //开启审计功能
        AuditManager.setAuditEnable(true)
        //设置 SQL 审计收集器
        AuditManager.setMessageCollector { auditMessage: AuditMessage ->
            log.debug(
                "[flex-sql spend {}ms] >>>>>>>>>> {} ", auditMessage.elapsedTime, auditMessage.getFullSql(),
            )
        }
    }
}
