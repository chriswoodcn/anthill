package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.math.BigDecimal

@ReadingConverter
class Decimal128ToBigDecimalConvert : Converter<Decimal128, BigDecimal> {
    override fun convert(source: Decimal128): BigDecimal {
        return source.bigDecimalValue()
    }
}
