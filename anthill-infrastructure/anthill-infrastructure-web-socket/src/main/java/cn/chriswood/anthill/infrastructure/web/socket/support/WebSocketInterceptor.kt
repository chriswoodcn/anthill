package cn.chriswood.anthill.infrastructure.web.socket.support

import cn.chriswood.anthill.infrastructure.web.auth.support.AuthHelper
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

/**
 * WebSocket握手请求的拦截器
 */
class WebSocketInterceptor : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val authUser = AuthHelper.getAuthUser()
        if (authUser == null){
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            response.flush()
            return false
        }
        attributes[WebSocketConstants.LOGIN_USER_KEY] = authUser
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
    }
}
