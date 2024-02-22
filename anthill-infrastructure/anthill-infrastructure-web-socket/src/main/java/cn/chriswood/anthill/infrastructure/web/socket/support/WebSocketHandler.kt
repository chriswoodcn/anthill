package cn.chriswood.anthill.infrastructure.web.socket.support

import cn.chriswood.anthill.infrastructure.web.auth.handler.AuthUser
import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler

/**
 * WebSocketHandler 实现类
 */
class WebSocketHandler : AbstractWebSocketHandler() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 连接建立后
     */
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val loginUser: AuthUser = session.attributes[WebSocketConstants.LOGIN_USER_KEY] as AuthUser
        WebSocketSessionHolder.addSession(
            "${loginUser.userType}:${loginUser.userId}",
            session
        )
        log.debug(
            "[WebSocketHandler] [afterConnectionEstablished] sessionId: {},userId:{},userType:{}",
            session.id,
            loginUser.userId,
            loginUser.userType
        )
    }

    /**
     * 连接关闭后
     */
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val loginUser: AuthUser = session.attributes[WebSocketConstants.LOGIN_USER_KEY] as AuthUser
        WebSocketSessionHolder.removeSession("${loginUser.userType}:${loginUser.userId}")
        log.debug(
            "[WebSocketHandler] [afterConnectionClosed] sessionId: {},userId:{},userType:{}",
            session.id,
            loginUser.userId,
            loginUser.userType
        )
    }

    /**
     * 处理发送来的文本消息
     */
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val handlerMap = SpringUtil.getBeansOfType(WebSocketTextMessageHandler::class.java)
        val list = handlerMap.toList()
        list.sortedBy { it.second.order }
        list.forEach { it.second.handle(session, message) }
    }

    /**
     * 心跳检测的回复
     */
    override fun handlePongMessage(session: WebSocketSession, message: PongMessage) {
        WebSocketUtil.sendPongMessage(session)
    }

    /**
     * 连接出错时
     */
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        log.error("[WebSocketHandler] [transport error] sessionId: {} , exception:{}", session.id, exception.message)
    }

    /**
     * 是否支持分片消息
     */
    override fun supportsPartialMessages(): Boolean {
        return false
    }
}
