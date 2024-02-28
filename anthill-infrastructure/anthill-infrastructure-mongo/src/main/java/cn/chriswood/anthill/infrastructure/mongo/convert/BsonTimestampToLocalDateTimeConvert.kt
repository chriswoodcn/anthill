package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.BsonTimestamp
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ReadingConverter
class BsonTimestampToLocalDateTimeConvert : Converter<BsonTimestamp, LocalDateTime> {
    override fun convert(source: BsonTimestamp): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.value), ZoneId.systemDefault())
    }
}
