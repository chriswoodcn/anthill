package cn.chriswood.anthill.infrastructure.web.auth.support

import cn.dev33.satoken.spring.SaTokenContextForSpringInJakartaServlet
import cn.dev33.satoken.spring.pathmatch.SaPatternsRequestConditionHolder
import org.springframework.context.annotation.Primary

@Primary
class SaTokenContextByPatternsRequestCondition : SaTokenContextForSpringInJakartaServlet() {
    override fun matchPath(pattern: String?, path: String?): Boolean {
        return SaPatternsRequestConditionHolder.match(pattern, path);
    }
}
