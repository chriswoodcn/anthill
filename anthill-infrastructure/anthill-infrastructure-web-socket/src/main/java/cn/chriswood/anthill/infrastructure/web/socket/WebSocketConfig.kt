package cn.chriswood.anthill.infrastructure.web.socket

import cn.chriswood.anthill.infrastructure.web.auth.AuthConfig
import cn.chriswood.anthill.infrastructure.web.socket.support.WebSocketHandler
import cn.chriswood.anthill.infrastructure.web.socket.support.WebSocketInterceptor
import cn.chriswood.anthill.infrastructure.web.socket.support.WebSocketProperties
import cn.chriswood.anthill.infrastructure.web.socket.support.WebSocketTopicListener
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.HandshakeInterceptor

@AutoConfiguration
@AutoConfigureAfter(AuthConfig::class)
@ConditionalOnProperty(value = ["anthill.websocket.enabled"], havingValue = "true")
@EnableConfigurationProperties(WebSocketProperties::class)
@EnableWebSocket
class WebSocketConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.debug(">>>>>>>>>> init WebSocketConfig >>>>>>>>>>")
    }

    @Bean
    @ConditionalOnBean(
        HandshakeInterceptor::class,
        WebSocketHandler::class,
        WebSocketTopicListener::class
    )
    fun webSocketConfigurer(
        handshakeInterceptor: HandshakeInterceptor,
        webSocketHandler: WebSocketHandler,
        webSocketProperties: WebSocketProperties
    ): WebSocketConfigurer {
        return WebSocketConfigurer { registry: WebSocketHandlerRegistry ->
            registry
                .addHandler(webSocketHandler, webSocketProperties.path)
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins(webSocketProperties.allowedOrigins)
        }
    }

    @Bean
    fun handshakeInterceptor(): HandshakeInterceptor {
        return WebSocketInterceptor()
    }

    @Bean
    fun webSocketHandler(): WebSocketHandler {
        return WebSocketHandler()
    }

    @Bean
    fun topicListener(): WebSocketTopicListener {
        return WebSocketTopicListener()
    }
}
