package cn.chriswood.anthill.infrastructure.mail.support

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EnhanceMailAccount(
    val key: String,
    val mailAccount: MailAccount
) {
    // 每日发送次数
    var dayCount: Int = 0

    // 每小时发送次数
    var hourCount: Int = 0

    // 每天发送限制
    var limitDayCount: Int = Int.MAX_VALUE

    // 每小时发送限制
    var limitHourCount: Int = Int.MAX_VALUE

    // 上一次发送的日期（天）初始化的时候即设置当天，用于滚动比较
    var lastSendDate: String = LocalDate.now()
        .format(DateTimeFormatter.ofPattern(Constants.PATTERN_YYYY_MM_DD))
    var lastSendHour: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(Constants.PATTERN_YYYY_MM_DD_HH))
}
