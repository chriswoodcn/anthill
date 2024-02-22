package cn.chriswood.anthill.infrastructure.web.socket.support

import cn.chriswood.anthill.infrastructure.redis.RedisUtil
import cn.chriswood.anthill.infrastructure.web.socket.constants.WebSocketConstants
import org.slf4j.LoggerFactory
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.function.Consumer

object WebSocketUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 发送消息
     *
     * @param sessionKey session主键 一般为用户id
     * @param message    消息文本
     */
    fun sendMessage(sessionKey: String, message: String?) {
        val session = WebSocketSessionHolder.getSessions(sessionKey)
        sendMessage(session, message)
    }

    /**
     * 订阅消息
     *
     * @param consumer 自定义处理
     */
    fun subscribeMessage(consumer: Consumer<WebSocketMessageDto>) {
        RedisUtil.subscribe(
            WebSocketConstants.WEB_SOCKET_TOPIC_COMMON,
            WebSocketMessageDto::class.java,
            consumer
        )
    }

    /**
     * 发布订阅的消息
     *
     * @param webSocketMessage 消息对象
     */
    fun publishMessage(webSocketMessage: WebSocketMessageDto) {
        val unsentSessionKeys: List<String> = mutableListOf()
        // 当前服务内session,直接发送消息
        for (sessionKey in webSocketMessage.sessionKeys) {
            if (WebSocketSessionHolder.existSession(sessionKey)) {
                sendMessage(sessionKey, webSocketMessage.message)
                continue
            }
            unsentSessionKeys.plus(sessionKey)
        }
        // 不在当前服务内session,发布订阅消息
        if (unsentSessionKeys.isNotEmpty()) {
            val broadcastMessage = WebSocketMessageDto(
                unsentSessionKeys,
                webSocketMessage.message
            )
            RedisUtil.publish(
                WebSocketConstants.WEB_SOCKET_TOPIC_COMMON,
                broadcastMessage
            ) {
                log.debug(
                    " WebSocket发送主题订阅消息topic:{} session keys:{} message:{}",
                    WebSocketConstants.WEB_SOCKET_TOPIC_COMMON,
                    unsentSessionKeys, webSocketMessage.message
                )
            }
        }
    }

    /**
     * 发布订阅的消息(群发)
     *
     * @param message 消息内容
     */
    fun publishAll(message: String?) {
        val broadcastMessage = WebSocketMessageDto(emptyList(), message)
        RedisUtil.publish(WebSocketConstants.WEB_SOCKET_TOPIC_COMMON, broadcastMessage) { consumer ->
            log.debug(
                "WebSocket发送主题订阅消息topic:{} session keys: all message:{}",
                WebSocketConstants.WEB_SOCKET_TOPIC_COMMON,
                message
            )
        }
    }

    fun sendPongMessage(session: WebSocketSession?) {
        sendMessage(session, PongMessage())
    }

    fun sendMessage(session: WebSocketSession?, message: String?) {
        sendMessage(session, TextMessage(message!!))
    }

    private fun sendMessage(session: WebSocketSession?, message: WebSocketMessage<*>) {
        if (session == null || !session.isOpen) {
            log.warn("[WebSocketUtil] [sendMessage] session会话已经关闭")
        } else {
            try {
                session.sendMessage(message)
            } catch (e: IOException) {
                log.error("[WebSocketUtil] [sendMessage] session({}) 发送消息({}) 异常", session, message, e)
            }
        }
    }
}
