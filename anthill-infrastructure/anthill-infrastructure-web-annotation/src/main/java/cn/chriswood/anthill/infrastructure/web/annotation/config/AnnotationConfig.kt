package cn.chriswood.anthill.infrastructure.web.annotation.config

import cn.chriswood.anthill.infrastructure.redis.RedisUtil
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
}
