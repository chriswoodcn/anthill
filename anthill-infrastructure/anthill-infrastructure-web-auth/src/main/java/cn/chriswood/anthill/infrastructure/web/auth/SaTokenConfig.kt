package cn.chriswood.anthill.infrastructure.web.auth

import cn.chriswood.anthill.infrastructure.core.factory.YmlPropertySourceFactory
import cn.chriswood.anthill.infrastructure.redis.RedisConfig
import cn.chriswood.anthill.infrastructure.web.auth.support.SaPermissionImpl
import cn.chriswood.anthill.infrastructure.web.auth.support.SaTokenContextByPatternsRequestCondition
import cn.chriswood.anthill.infrastructure.web.auth.support.SaTokenDaoImpl
import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpLogic
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@AutoConfiguration
@AutoConfigureAfter(RedisConfig::class)
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = "anthill.web.auth",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@PropertySource(value = ["classpath:common-satoken.yml"], factory = YmlPropertySourceFactory::class)
@Import(SaTokenContextByPatternsRequestCondition::class)
class SaTokenConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init SaTokenConfig >>>>>>>>>>")
    }

    /**
     * Sa-Token 整合 jwt (简单模式)
     */
    @Bean
    fun getStpLogicJwt(): StpLogic {
        return StpLogicJwtForSimple()
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    fun stpInterface(): StpInterface {
        return SaPermissionImpl()
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    fun saTokenDao(): SaTokenDao {
        return SaTokenDaoImpl()
    }
}
