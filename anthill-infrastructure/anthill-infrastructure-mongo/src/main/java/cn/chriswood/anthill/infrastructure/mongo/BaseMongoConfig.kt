package cn.chriswood.anthill.infrastructure.mongo

import cn.chriswood.anthill.infrastructure.mongo.convert.BigDecimalToDecimal128Convert
import cn.chriswood.anthill.infrastructure.mongo.convert.BsonTimestampToLocalDateTimeConvert
import cn.chriswood.anthill.infrastructure.mongo.convert.Decimal128ToBigDecimalConvert
import cn.chriswood.anthill.infrastructure.mongo.convert.LocalDateTimeToBsonTimestampConvert
import cn.chriswood.anthill.infrastructure.mongo.support.MongoConfigProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@AutoConfiguration
@EnableConfigurationProperties(MongoConfigProperties::class)
@ConditionalOnExpression(
    "#{'true'.equals(environment.getProperty('anthill.mongo.enabled'))}"
)
class BaseMongoConfig {
    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions.create {
            it.registerConverters(
                mutableListOf(
                    LocalDateTimeToBsonTimestampConvert(),
                    BsonTimestampToLocalDateTimeConvert(),
                    BigDecimalToDecimal128Convert(),
                    Decimal128ToBigDecimalConvert()
                )
            )
        }
    }

    @Bean
    fun mongoMappingContext(): MongoMappingContext {
        val context = MongoMappingContext()
        context.isAutoIndexCreation = true
        context.setFieldNamingStrategy(SnakeCaseFieldNamingStrategy())
        return context
    }
}
