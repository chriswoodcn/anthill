package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.BsonTimestamp
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class LongToBsonTimestampConvert : Converter<Long, BsonTimestamp> {
    override fun convert(source: Long): BsonTimestamp {
        return BsonTimestamp(source)
    }
}
