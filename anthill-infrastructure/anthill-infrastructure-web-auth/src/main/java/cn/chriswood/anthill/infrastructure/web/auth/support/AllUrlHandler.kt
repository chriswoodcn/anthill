package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.hutool.core.util.ReUtil
import cn.hutool.extra.spring.SpringUtil
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.util.pattern.PathPattern
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern

class AllUrlHandler : InitializingBean {

    private val PATTERN = Pattern.compile("\\{(.*?)\\}")

    val urls: List<String> = arrayListOf()
    override fun afterPropertiesSet() {
        val set: MutableSet<String> = HashSet()
        val mapping: RequestMappingHandlerMapping = SpringUtil.getBean(
            "requestMappingHandlerMapping",
            RequestMappingHandlerMapping::class.java
        )
        val map = mapping.getHandlerMethods()
        map.keys.forEach(Consumer<RequestMappingInfo> { info: RequestMappingInfo ->
            // 获取注解上边的 path 替代 path variable 为 *
            Objects.requireNonNull<Set<PathPattern>>(
                info.pathPatternsCondition!!.patterns
            )
                .forEach(Consumer { url: PathPattern ->
                    set.add(
                        ReUtil.replaceAll(
                            url.patternString,
                            PATTERN,
                            "*"
                        )
                    )
                })
        })
        urls.plus(set)
    }
}
