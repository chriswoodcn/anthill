package cn.chriswood.anthill.infrastructure.web.annotation.log

import org.aspectj.lang.JoinPoint

interface LogAppender {
    fun print(joinPoint: JoinPoint, annotation: Log, e: Exception?, res: Any?)
}
