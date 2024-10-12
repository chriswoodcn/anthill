package cn.chriswood.anthill.infrastructure.web.annotation.repeatLimit

import cn.chriswood.anthill.infrastructure.core.utils.ServletUtil
import cn.chriswood.anthill.infrastructure.web.annotation.support.CacheKeys
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.chriswood.anthill.infrastructure.json.utils.JacksonUtil
import cn.chriswood.anthill.infrastructure.redis.utils.RedisUtil
import cn.chriswood.anthill.infrastructure.web.annotation.support.AspectUtil
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.infrastructure.web.exception.InfrastructureWebExceptionEnum
import cn.dev33.satoken.SaManager
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.crypto.SecureUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

@Aspect
class RepeatLimitAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    private val KEY_CACHE = ThreadLocal<String>()

    @Before("@annotation(repeatSubmit)")
    @Throws(Throwable::class)
    fun doBefore(point: JoinPoint, repeatSubmit: RepeatLimit) {
        // 如果注解不为0 则使用注解数值
        var interval: Long = repeatSubmit.timeUnit.toSeconds(repeatSubmit.interval)
        if (interval <= 0) interval = 100 //至少100ms
        val request = ServletUtil.getRequest() ?: return
        val nowParams = argsArrayToString(point.args)
        // 请求地址（作为存放cache的key值）
        val url: String = request.requestURI
        // header中取token
        val header = request.getHeader(SaManager.getConfig().tokenName) ?: return
        val token = StringUtil.trimToEmpty(header)
        // 拼接参数
        val submitKey = SecureUtil.md5("$token:$nowParams")
        // 唯一标识（指定key + url + 消息头）
        val cacheRepeatKey = "${CacheKeys.REPEAT_SUBMIT_KEY}$url:$submitKey"
        if (RedisUtil.setObjectIfAbsent(cacheRepeatKey, "", Duration.ofSeconds(interval))) {
            KEY_CACHE.set(cacheRepeatKey)
        } else {
            InfrastructureWebExceptionEnum.REPEAT_SUBMIT.eject(interval)
        }
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(
        pointcut = "@annotation(repeatSubmit)",
        returning = "result"
    )
    fun doAfterReturning(joinPoint: JoinPoint, repeatSubmit: RepeatLimit, result: Any?) {
        if (result is R<*>) {
            try {
                // 成功则不删除redis数据 保证在有效时间内无法重复提交
                if (result.code == R.SUCCESS_CODE) {
                    return
                }
                RedisUtil.deleteObject(KEY_CACHE.get())
            } finally {
                KEY_CACHE.remove()
            }
        }
    }

    /**
     * 参数拼装
     */
    private fun argsArrayToString(paramsArray: Array<Any>): String {
        val params = StringJoiner(" ")
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString()
        }
        for (o in paramsArray) {
            if (ObjectUtil.isNotNull(o) && !AspectUtil.isFilterObject(o)) {
                params.add(JacksonUtil.bean2string(o))
            }
        }
        return params.toString()
    }
}
