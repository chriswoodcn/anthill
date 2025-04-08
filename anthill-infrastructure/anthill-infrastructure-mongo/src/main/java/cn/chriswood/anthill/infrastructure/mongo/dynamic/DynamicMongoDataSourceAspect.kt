package cn.chriswood.anthill.infrastructure.mongo.dynamic

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order

@Aspect
@Order(1)
class DynamicMongoDataSourceAspect {
    @Around("@annotation(dataSource)")
    fun around(joinPoint: ProceedingJoinPoint, dataSource: DynamicMongoDataSource): Any? {
        val previous = DynamicMongoContextHolder.getDatabase()
        try {
            DynamicMongoContextHolder.setDatabase(dataSource.value)
            return joinPoint.proceed()
        } finally {
            previous?.let { DynamicMongoContextHolder.setDatabase(it) }
                ?: DynamicMongoContextHolder.clearDatabase()
        }
    }
}
