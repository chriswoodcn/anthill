package cn.chriswood.anthill.infrastructure.mongo.convert

import org.bson.types.Decimal128
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.math.BigDecimal

@WritingConverter
class BigDecimalToDecimal128Convert : Converter<BigDecimal, Decimal128> {
    override fun convert(source: BigDecimal): Decimal128 {
        return Decimal128(source)
    }
}
