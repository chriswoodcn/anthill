package cn.chriswood.anthill.infrastructure.web.annotation.log

import org.aspectj.lang.JoinPoint
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Component

/**
 * 默认实现的标准输出
 */
@Component
@ConditionalOnMissingBean(LogAppender::class)
class StandardLogAppender : LogAppender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun print(joinPoint: JoinPoint, annotation: Log, e: Exception?, res: Any?) {
        log.debug(
            "[MODULE:{}] [USER:{}] [OPERATE:{}] error:{} res:{}",
            annotation.module,
            annotation.user,
            annotation.operate.name,
            e,
            res
        )
    }
}
