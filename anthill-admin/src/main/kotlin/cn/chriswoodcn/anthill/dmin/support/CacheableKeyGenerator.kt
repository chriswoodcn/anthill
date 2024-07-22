package com.taotao.bmm.support

import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Component
import java.lang.reflect.Method

/**
 * 接口缓存使用的KeyGenerator
 */
@Component
class CacheableGetRoutersKeyGenerator : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        return AuthHelper.getUserId()
    }
}

@Component
class CacheableMenuSelectKeyGenerator : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        println(target)
        println(params)
        return "${AuthHelper.getUserId()}:${params[0]}"
    }
}