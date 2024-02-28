package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.BsonTimestamp
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

@WritingConverter
class LocalDateTimeToBsonTimestampConvert : Converter<LocalDateTime, BsonTimestamp> {
    override fun convert(source: LocalDateTime): BsonTimestamp {
        return BsonTimestamp(source.toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}
