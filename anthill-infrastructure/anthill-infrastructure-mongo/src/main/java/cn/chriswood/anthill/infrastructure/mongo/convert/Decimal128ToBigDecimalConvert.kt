package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import java.math.BigDecimal

class Decimal128ToBigDecimalConvert : Converter<Decimal128, BigDecimal> {
    override fun convert(source: Decimal128): BigDecimal {
        return source.bigDecimalValue()
    }
}
