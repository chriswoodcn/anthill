package cn.chriswood.anthill.infrastructure.reactive.web.support

import org.springframework.core.Ordered
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class ReactiveWebFilter : WebFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun getOrder(): Int {
        return Ordered.HIGHEST_PRECEDENCE + 1
    }
}
