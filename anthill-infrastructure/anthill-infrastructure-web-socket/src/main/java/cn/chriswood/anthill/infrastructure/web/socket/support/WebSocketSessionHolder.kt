package cn.chriswood.anthill.infrastructure.web.socket.support

import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * WebSocket会话持有者
 */
object WebSocketSessionHolder {
    private val USER_SESSION_MAP: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    fun addSession(sessionKey: String, session: WebSocketSession) {
        USER_SESSION_MAP[sessionKey] = session
    }

    fun removeSession(sessionKey: String) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.remove(sessionKey)
        }
    }

    fun getSessions(sessionKey: String): WebSocketSession? {
        return USER_SESSION_MAP[sessionKey]
    }

    fun getSessionsAll(): Set<String> {
        return USER_SESSION_MAP.keys
    }

    fun existSession(sessionKey: String): Boolean {
        return USER_SESSION_MAP.containsKey(sessionKey)
    }
}
