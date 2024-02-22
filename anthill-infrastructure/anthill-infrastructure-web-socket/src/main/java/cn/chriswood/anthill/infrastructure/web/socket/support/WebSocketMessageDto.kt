package cn.chriswood.anthill.infrastructure.web.socket.support

data class WebSocketMessageDto(
    /**
     * 需要推送到的session key 列表
     */
    var sessionKeys: List<String> = emptyList(),

    /**
     * 需要发送的消息
     */
    var message: String? = null
)
