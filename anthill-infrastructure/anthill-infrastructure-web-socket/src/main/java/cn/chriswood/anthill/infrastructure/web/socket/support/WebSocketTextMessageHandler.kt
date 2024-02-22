package cn.chriswood.anthill.infrastructure.web.socket.support

import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

/**
 * WebSocket TextMessage处理器
 */
interface WebSocketTextMessageHandler {

    val order: Int
    fun handle(session: WebSocketSession, message: TextMessage)
}
