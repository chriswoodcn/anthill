package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.BsonTimestamp
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class BsonTimestampToLongConvert : Converter<BsonTimestamp, Long> {
    override fun convert(source: BsonTimestamp): Long {
        return source.value
    }
}
