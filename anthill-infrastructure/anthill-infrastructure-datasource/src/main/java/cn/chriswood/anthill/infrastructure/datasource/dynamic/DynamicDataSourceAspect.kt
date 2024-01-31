package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.hibernate.engine.spi.SessionImplementor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.annotation.Order
import org.springframework.orm.hibernate5.SessionFactoryUtils
import org.springframework.stereotype.Component


/**
 * 切换数据源Advice
 */
@Aspect
@Order(-1)//设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中)
@Component
@ConditionalOnProperty(
    prefix = "anthill.jpa",
    name = ["type"],
    havingValue = Constants.DYNAMIC,
)
class DynamicDataSourceAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    @PersistenceContext
    private val entityManager: EntityManager? = null

    @Before(value = "@annotation(source)")
    fun changeDataSource(point: JoinPoint?, source: DDS) {
        val currentSource = source.value
        if (DynamicDataSourceContextHolder.containsDataSourceType(currentSource)) {
            log.info("切换数据源->[$currentSource]")
            DynamicDataSourceContextHolder.setDataSourceType(currentSource)
        } else {
            log.info("数据源[$currentSource]不存在，使用默认数据源->[${Constants.PRIMARY}]")
            DynamicDataSourceContextHolder.setDataSourceType(Constants.PRIMARY)
        }
    }

    @After(value = "@annotation(source)")
    fun restoreDataSource(point: JoinPoint?, source: DDS?) {
        DynamicDataSourceContextHolder.setDataSourceType(Constants.PRIMARY)
        if (entityManager == null) return
        val session = entityManager.unwrap(SessionImplementor::class.java)
        SessionFactoryUtils.closeSession(session)
    }
}
