package cn.chriswood.anthill.infrastructure.web.annotation

import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.chriswood.anthill.infrastructure.web.annotation.rateLimit.RateLimitAspect
import cn.chriswood.anthill.infrastructure.web.annotation.repeatLimit.RepeatLimitAspect
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean

@AutoConfiguration
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
