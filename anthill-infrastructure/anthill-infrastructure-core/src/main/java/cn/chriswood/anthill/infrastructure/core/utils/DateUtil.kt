package cn.chriswood.anthill.infrastructure.core.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object DateUtil {
    fun tranLocalDateTime2UTCTime(localDateTime: LocalDateTime): ZonedDateTime {
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
    }

    fun tranUTCTime2LocalDateTime(utcTime: ZonedDateTime): LocalDateTime {
        val zoneId = ZoneId.systemDefault()
        return utcTime.withZoneSameInstant(zoneId).toLocalDateTime()
    }
}
