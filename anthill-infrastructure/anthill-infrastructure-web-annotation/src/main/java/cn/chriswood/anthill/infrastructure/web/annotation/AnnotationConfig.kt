package cn.chriswood.anthill.infrastructure.web.annotation

import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.chriswood.anthill.infrastructure.web.annotation.rateLimit.RateLimitAspect
import cn.chriswood.anthill.infrastructure.web.annotation.repeatLimit.RepeatLimitAspect
import cn.chriswood.anthill.infrastructure.web.auth.AuthConfig
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean

@AutoConfiguration
@AutoConfigureAfter(AuthConfig::class)
@ConditionalOnWebApplication
class AnnotationConfig {
    @Bean
    @ConditionalOnClass(RedisUtil::class)
    fun repeatLimitAspect(): RepeatLimitAspect {
        return RepeatLimitAspect()
    }

    @Bean
    @ConditionalOnClass(RedisUtil::class)
    fun rateLimitAspect(): RateLimitAspect {
        return RateLimitAspect()
    }
}
