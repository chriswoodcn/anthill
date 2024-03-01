package cn.chriswood.anthill.infrastructure.web.annotation.log

import org.aspectj.lang.JoinPoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 默认实现的标准输出
 */
@Component
class StandardLogAppender : LogAppender {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun print(joinPoint: JoinPoint, annotation: Log, e: Exception?, res: Any?) {
        if (e == null)
            log.debug(
                "[MODULE:{}] [USER:{}] [OPERATE:{}] res:{}",
                annotation.module,
                annotation.user,
                annotation.operate.name,
                res
            )
        else
            log.error(
                "[MODULE:{}] [USER:{}] [OPERATE:{}] error:{}",
                annotation.module,
                annotation.user,
                annotation.operate.name,
                e.message
            )
    }
}
