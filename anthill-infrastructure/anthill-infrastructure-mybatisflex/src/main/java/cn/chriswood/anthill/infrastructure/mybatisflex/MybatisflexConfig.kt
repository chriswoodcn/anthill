package cn.chriswood.anthill.infrastructure.mybatisflex

import cn.chriswood.anthill.infrastructure.mybatisflex.support.FlexInsertListener
import cn.chriswood.anthill.infrastructure.mybatisflex.support.FlexUpdateListener
import com.mybatisflex.core.FlexGlobalConfig
import com.mybatisflex.core.audit.AuditManager
import com.mybatisflex.core.audit.AuditMessage
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration


@AutoConfiguration
class MybatisflexConfig : MyBatisFlexCustomizer {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun customize(config: FlexGlobalConfig) {
        // 指定逻辑删除字段名
        config.logicDeleteColumn = "del_flag"
        config.registerInsertListener(FlexInsertListener())
        config.registerUpdateListener(FlexUpdateListener())

        //开启审计功能
        AuditManager.setAuditEnable(true)
        //设置 SQL 审计收集器
        AuditManager.setMessageCollector { auditMessage: AuditMessage ->
            log.info(
                "[mybatis-flex-sql] >>>>>>>>>> {},{}ms", auditMessage.getFullSql(), auditMessage.elapsedTime
            )
        }
    }
}
