package cn.chriswood.anthill.infrastructure.web.annotation.rateLimit

import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.chriswood.anthill.infrastructure.web.annotation.support.CacheKeys
import cn.chriswood.anthill.infrastructure.web.exception.InfrastructureWebExceptionEnum
import cn.chriswood.anthill.infrastructure.web.utils.ServletUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RateType
import org.slf4j.LoggerFactory
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.ParameterNameDiscoverer
import org.springframework.expression.EvaluationContext
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.ParserContext
import org.springframework.expression.common.TemplateParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

@Aspect
class RateLimitAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 定义spel表达式解析器
     */
    private val parser: ExpressionParser = SpelExpressionParser()

    /**
     * 定义spel解析模版
     */
    private val parserContext: ParserContext = TemplateParserContext()

    /**
     * 定义spel上下文对象进行解析
     */
    private val context: EvaluationContext = StandardEvaluationContext()

    /**
     * 方法参数解析器
     */
    private val pnd: ParameterNameDiscoverer = DefaultParameterNameDiscoverer()

    @Before("@annotation(rateLimit)")
    @Throws(Throwable::class)
    fun doBefore(point: JoinPoint, rateLimit: RateLimit) {
        val time: Int = rateLimit.time
        val count: Int = rateLimit.count
        val combineKey = getCombineKey(rateLimit, point)
        try {
            var rateType = RateType.OVERALL
            if (rateLimit.type == RateLimitType.CLUSTER) {
                rateType = RateType.PER_CLIENT
            }
            val number: Long = RedisUtil.rateLimit(combineKey, rateType, count, time)
            if (number == -1L) {
                InfrastructureWebExceptionEnum.RATE_LIMIT.eject()
            }
            log.info("限制令牌 => {}, 剩余令牌 => {}, 缓存key => '{}'", count, number, combineKey)
        } catch (e: Exception) {
            InfrastructureWebExceptionEnum.RATE_LIMIT.eject()
        }
    }

    private fun getCombineKey(rateLimit: RateLimit, point: JoinPoint): String {
        var key: String = rateLimit.key
        // 获取方法(通过方法签名来获取)
        val signature = point.signature as MethodSignature
        val method = signature.method
        val targetClass = method.declaringClass
        // 判断是否是spel格式
        if (StringUtil.containsAny(key, "#")) {
            // 获取参数值
            val args = point.args
            // 获取方法上参数的名称
            val parameterNames = pnd.getParameterNames(method)
            if (parameterNames.isNullOrEmpty()) {
                InfrastructureWebExceptionEnum.FUNC_ERROR.eject(
                    "RateLimitAspect", "getCombineKey", "限流key解析异常"
                )
            }
            for (i in parameterNames!!.indices) {
                context.setVariable(parameterNames[i], args[i])
            }
            // 解析返回给key
            try {
                val expression: Expression = if (StringUtil.startsWith(key, parserContext.expressionPrefix)
                    && StringUtil.endsWith(key, parserContext.expressionSuffix)
                ) {
                    parser.parseExpression(key, parserContext)
                } else {
                    parser.parseExpression(key)
                }
                key = expression.getValue(context, String::class.java) + ":"
            } catch (e: Exception) {
                InfrastructureWebExceptionEnum.FUNC_ERROR.eject(
                    "RateLimitAspect", "getCombineKey", e.message
                )
            }
        }
        val stringBuffer: StringBuilder = StringBuilder(CacheKeys.RATE_LIMIT_KEY)
        stringBuffer.append(ServletUtil.getRequest()!!.requestURI).append(":")
        if (rateLimit.type == RateLimitType.IP) {
            // 获取请求ip
            stringBuffer.append(ServletUtil.getClientIP()).append(":")
        } else if (rateLimit.type == RateLimitType.CLUSTER) {
            // 获取客户端实例id
            stringBuffer.append(RedisUtil.getClient().id).append(":")
        }
        return stringBuffer.append(key).toString()
    }
}
