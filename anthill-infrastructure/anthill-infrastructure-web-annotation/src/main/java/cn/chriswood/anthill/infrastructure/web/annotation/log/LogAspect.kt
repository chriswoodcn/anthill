package cn.chriswood.anthill.infrastructure.web.annotation.log

import cn.hutool.extra.spring.SpringUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration

@Aspect
class LogAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "res")
    fun doAfterReturning(joinPoint: JoinPoint, controllerLog: Log, res: Any?) {
        log.debug("触发@Log切面 AfterReturning")
        handleLog(joinPoint, controllerLog, null, res)
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    fun doAfterThrowing(joinPoint: JoinPoint, controllerLog: Log, e: Exception?) {
        log.debug("触发@Log切面 AfterThrowing")
        handleLog(joinPoint, controllerLog, e, null)
    }

    /**
     * 借助springboot ioc容器寻找实现类进行日志输出
     */
    private fun handleLog(joinPoint: JoinPoint, controllerLog: Log, e: Exception?, res: Any?) {
        val appenderMap = SpringUtil.getBeansOfType(controllerLog.appender.java) ?: return
        val appenderList = appenderMap.map { it.value }.toList()
        if (appenderList.isEmpty()) return
        appenderList.forEach { it.print(joinPoint, controllerLog, e, res) }
    }
}
