package cn.chriswood.anthill.infrastructure.annotation.repeatLimit

import cn.chriswood.anthill.infrastructure.annotation.constants.CacheKeys
import cn.chriswood.anthill.infrastructure.core.utils.StringUtil
import cn.chriswood.anthill.infrastructure.json.JacksonUtil
import cn.chriswood.anthill.infrastructure.redis.RedisUtil
import cn.chriswood.anthill.infrastructure.web.base.R
import cn.chriswood.anthill.infrastructure.web.exception.InfrastructureWebExceptionEnum
import cn.chriswood.anthill.infrastructure.web.utils.ServletUtil
import cn.dev33.satoken.SaManager
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.validation.BindingResult
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*

@Aspect
class RepeatLimitAspect {

    private val KEY_CACHE = ThreadLocal<String>()

    @Before("@annotation(repeatSubmit)")
    @Throws(Throwable::class)
    fun doBefore(point: JoinPoint, repeatSubmit: RepeatLimit) {
        // 如果注解不为0 则使用注解数值
        val interval: Long = repeatSubmit.timeUnit.toSeconds(repeatSubmit.interval)
        InfrastructureWebExceptionEnum.REPEAT_SUBMIT.stateFalse(interval < 1, 1)
        val request: HttpServletRequest = ServletUtil.getRequest() ?: return
        val nowParams: String = argsArrayToString(point.args)
        // 请求地址（作为存放cache的key值）
        val url: String = request.requestURI
        // 唯一值（没有消息头则使用请求地址）
        var submitKey: String = StringUtil.trimToEmpty(request.getHeader(SaManager.getConfig().tokenName))
        submitKey = cn.hutool.crypto.SecureUtil.md5("$submitKey:$nowParams")
        // 唯一标识（指定key + url + 消息头）
        val cacheRepeatKey: String = CacheKeys.REPEAT_SUBMIT_KEY + url + submitKey
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
    @AfterReturning(pointcut = "@annotation(repeatSubmit)", returning = "res")
    fun doAfterReturning(joinPoint: JoinPoint?, repeatSubmit: RepeatLimit?, res: Any?) {
        if (res is R<*>) {
            try {
                // 成功则不删除redis数据 保证在有效时间内无法重复提交
                if (res.code == R.SUCCESS_CODE) {
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
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                params.add(JacksonUtil.bean2string(o))
            }
        }
        return params.toString()
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    private fun isFilterObject(o: Any): Boolean {
        val clazz: Class<*> = o.javaClass
        if (clazz.isArray) {
            return clazz.componentType.isAssignableFrom(MultipartFile::class.java)
        } else if (MutableCollection::class.java.isAssignableFrom(clazz)) {
            val collection = o as Collection<*>
            for (value in collection) {
                return value is MultipartFile
            }
        } else if (MutableMap::class.java.isAssignableFrom(clazz)) {
            val map = o as Map<*, *>
            for (value in map.values) {
                return value is MultipartFile
            }
        }
        return (o is MultipartFile || o is HttpServletRequest || o is HttpServletResponse
            || o is BindingResult)
    }
}
