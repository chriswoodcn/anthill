package cn.chriswood.anthill.infrastructure.mail.support

import java.time.LocalDate

class EnhanceMailAccount(
    val mailAccount: MailAccount
) {
    // 发送总次数
    var sendCount: Int = 0

    // 上一次发送的日期（天）初始化的时候即设置当天，用于滚动比较
    var lastSendTime: LocalDate = LocalDate.now()

    // 每天发送限制 -1代表无限制
    var limitCount: Int = -1
}
