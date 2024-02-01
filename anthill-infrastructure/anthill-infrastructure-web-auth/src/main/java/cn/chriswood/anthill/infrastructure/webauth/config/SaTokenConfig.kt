package cn.chriswood.anthill.infrastructure.webauth.config

import cn.chriswood.anthill.framework.factory.YmlPropertySourceFactory
import cn.chriswood.anthill.infrastructure.webauth.handler.SaPermissionImpl
import cn.chriswood.anthill.infrastructure.webauth.handler.SaTokenDaoImpl
import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpLogic
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = "anthill.web.auth",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@PropertySource(value = ["classpath:common-satoken.yml"], factory = YmlPropertySourceFactory::class)
class SaTokenConfig {

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
