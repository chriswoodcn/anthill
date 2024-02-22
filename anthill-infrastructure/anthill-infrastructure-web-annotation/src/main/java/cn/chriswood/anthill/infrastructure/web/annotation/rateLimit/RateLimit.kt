package cn.chriswood.anthill.infrastructure.web.annotation.rateLimit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RateLimit(
    /**
     * 限流key,支持使用Spring el表达式来动态获取方法上的参数值
     * 格式类似于  #code.id #{#code}
     */
    val key: String = "",

    /**
     * 限流时间,单位秒
     */
    val time: Int = 60,

    /**
     * 限流次数
     */
    val count: Int = 100,

    /**
     * 限流类型
     */
    val type: RateLimitType = RateLimitType.DEFAULT,

    /**
     * 提示消息支持国际化
     */
    val dialect: String = "InfrastructureWeb.RATE_LIMIT",

    /**
     * 默认消息
     */
    val message: String = "forbid repeat submit",
)
