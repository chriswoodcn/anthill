package cn.chriswood.anthill.infrastructure.webauth.config

import cn.chriswood.anthill.framework.factory.YmlPropertySourceFactory
import cn.chriswood.anthill.infrastructure.redis.RedisConfig
import cn.chriswood.anthill.infrastructure.webauth.exception.WebAuthExceptionHandler
import cn.chriswood.anthill.infrastructure.webauth.handler.SaPermissionImpl
import cn.chriswood.anthill.infrastructure.webauth.handler.SaTokenDaoImpl
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


    /**
     * Sa-Token 整合 jwt (简单模式)
     */
    @Bean
    fun getStpLogicJwt(): StpLogic {
        val stpLogicJwtForSimple = StpLogicJwtForSimple()
        log.debug(">>>>>>>>>> init SaTokenConfig StpLogicJwt >>>>>>>>>>")
        return stpLogicJwtForSimple
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    fun stpInterface(): StpInterface {
        val saPermissionImpl = SaPermissionImpl()
        log.debug(">>>>>>>>>> init SaTokenConfig StpInterface >>>>>>>>>>")
        return saPermissionImpl
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    fun saTokenDao(): SaTokenDao {
        val saTokenDaoImpl = SaTokenDaoImpl()
        log.debug(">>>>>>>>>> init SaTokenConfig SaTokenDao >>>>>>>>>>")
        return saTokenDaoImpl
    }
}
