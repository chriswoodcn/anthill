package cn.chriswood.anthill.infrastructure.json.utils

import cn.chriswood.anthill.infrastructure.json.support.BigNumberSerializer
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object JacksonUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 默认日期时间格式
     */
    const val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"

    /**
     * 默认日期格式
     */
    const val dateFormat = "yyyy-MM-dd"

    /**
     * 默认时间格式
     */
    const val timeFormat = "HH:mm:ss"


    fun buildJavaTimeModule(): SimpleModule {
        // JSR 310日期时间处理
        val javaTimeModule = JavaTimeModule()
        val formatter =
            DateTimeFormatter.ofPattern(dateTimeFormat)
        javaTimeModule.addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(formatter)
        )
        javaTimeModule.addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(formatter)
        )
        val dateFormatter =
            DateTimeFormatter.ofPattern(dateFormat)
        javaTimeModule.addSerializer(
            LocalDate::class.java,
            LocalDateSerializer(dateFormatter)
        )
        javaTimeModule.addDeserializer(
            LocalDate::class.java,
            LocalDateDeserializer(dateFormatter)
        )
        val timeFormatter =
            DateTimeFormatter.ofPattern(timeFormat)
        javaTimeModule.addSerializer(
            LocalTime::class.java,
            LocalTimeSerializer(timeFormatter)
        )
        javaTimeModule.addDeserializer(
            LocalTime::class.java,
            LocalTimeDeserializer(timeFormatter)
        )
        return javaTimeModule
    }

    fun buildBigNumberModule(): SimpleModule {
        // 全局转化Long类型为String，解决序列化后传入前端Long类型精度丢失问题
        val bigNumberModule = SimpleModule()
        bigNumberModule.addSerializer(Long::class.java, BigNumberSerializer.instance)
        bigNumberModule.addSerializer(java.lang.Long.TYPE, BigNumberSerializer.instance)
        bigNumberModule.addSerializer(BigInteger::class.java, BigNumberSerializer.instance)
        bigNumberModule.addSerializer(BigDecimal::class.java, ToStringSerializer.instance)
        return bigNumberModule
    }

    var objectMapper: ObjectMapper

    init {
        objectMapper = ObjectMapper().registerModules(buildJavaTimeModule(), buildBigNumberModule())
    }

    fun replaceObjectMapper(om: ObjectMapper) {
        objectMapper = om
    }

    fun bean2string(o: Any?): String? {
        return try {
            objectMapper.writeValueAsString(o)
        } catch (e: JsonProcessingException) {
            log.error("JacksonUtil bean2string error {}", e.message)
            return null
        }
    }

    fun <T> string2bean(o: String?): T? {
        return try {
            objectMapper.readValue(o, object : TypeReference<T>() {})
        } catch (e: JsonProcessingException) {
            log.error("JacksonUtil string2bean error {}", e.message)
            return null
        }
    }

    fun <T> string2list(o: String?): List<T> {
        return try {
            objectMapper.readValue(o, object : TypeReference<List<T>>() {})
        } catch (e: JsonProcessingException) {
            log.error("JacksonUtil string2list error {}", e.message)
            emptyList()
        }
    }

    fun <K, V> string2map(o: String?): Map<K, V> {
        return try {
            objectMapper.readValue(o, object : TypeReference<Map<K, V>>() {})
        } catch (e: JsonProcessingException) {
            log.error("JacksonUtil string2map error {}", e.message)
            emptyMap()
        }
    }
}
