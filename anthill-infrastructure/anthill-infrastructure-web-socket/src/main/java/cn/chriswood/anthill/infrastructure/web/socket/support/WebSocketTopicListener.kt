package cn.chriswood.anthill.infrastructure.web.socket.support

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.Ordered

class WebSocketTopicListener : ApplicationRunner, Ordered {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun run(args: ApplicationArguments?) {
        WebSocketUtil.subscribeMessage { message ->
            log.debug(
                "WebSocket主题订阅收到消息session keys={} message={}",
                message.sessionKeys,
                message.message
            )
            // 如果key不为空就按照key发消息 如果为空就群发
            if (message.sessionKeys.isNotEmpty()) {
                message.sessionKeys.forEach { key ->
                    if (WebSocketSessionHolder.existSession(key)) {
                        WebSocketUtil.sendMessage(key, message.message)
                    }
                }
            } else {
                WebSocketSessionHolder.getSessionsAll()
                    .forEach { key -> WebSocketUtil.sendMessage(key, message.message) }
            }
        }
        log.debug(">>>>>>>>>> init WebSocketTopicListener >>>>>>>>>>")
    }

    override fun getOrder(): Int {
        return -1
    }
}
