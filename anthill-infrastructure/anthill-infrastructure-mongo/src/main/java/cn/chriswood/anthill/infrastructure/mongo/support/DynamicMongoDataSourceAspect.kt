package cn.chriswood.anthill.infrastructure.mongo.support

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class DynamicMongoDataSourceAspect {
    @Around("@annotation(dataSource)")
    fun around(joinPoint: ProceedingJoinPoint, dataSource: DynamicMongoDataSource): Any? {
        val previous = DynamicMongoContextHolder.getDatabase()
        try {
            DynamicMongoContextHolder.setDatabase(dataSource.value)
            return joinPoint.proceed()
        } finally {
            previous?.let { DynamicMongoContextHolder.setDatabase(it) }
                ?: DynamicMongoContextHolder.clear()
        }
    }
}
