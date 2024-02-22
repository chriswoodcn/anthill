package cn.chriswood.anthill.infrastructure.web.auth.config

import cn.dev33.satoken.`fun`.SaFunction
import cn.dev33.satoken.interceptor.SaInterceptor
import cn.dev33.satoken.router.SaRouter
import cn.dev33.satoken.stp.StpUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@AutoConfiguration
@AutoConfigureAfter(SaTokenConfig::class)
@EnableConfigurationProperties(AuthProperties::class)
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = "anthill.web.auth",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class AuthConfig(
    private val authProperties: AuthProperties
) : WebMvcConfigurer {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun addInterceptors(registry: InterceptorRegistry) {
        val saInterceptor = SaInterceptor {
            // val allUrlHandler: AllUrlHandler = SpringUtil.getBean(AllUrlHandler::class.java)
            // 登录验证 -- 排除多个路径
            SaRouter
                // 获取所有的web路径
                .match("/**")
                .notMatch(authProperties.excludes ?: emptyList())
                // 对未排除的路径进行检查
                .check(SaFunction {
                    StpUtil.checkLogin()
                    if (log.isDebugEnabled) {
                        log.info("剩余有效时间: {}", StpUtil.getTokenTimeout());
                        log.info("临时有效时间: {}", StpUtil.getTokenActiveTimeout());
                    }
                })
        }
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(saInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(authProperties.excludes ?: emptyList())

        log.debug(">>>>>>>>>> init AuthConfig addInterceptors >>>>>>>>>>")
    }
}
