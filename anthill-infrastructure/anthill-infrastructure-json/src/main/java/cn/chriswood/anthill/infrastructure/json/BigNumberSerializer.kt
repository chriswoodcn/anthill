package cn.chriswood.anthill.infrastructure.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl
import com.fasterxml.jackson.databind.ser.std.NumberSerializer
import java.io.IOException

@JacksonStdImpl
class BigNumberSerializer(rawType: Class<out Number?>?) : NumberSerializer(rawType) {

    companion object {
        private const val MAX_SAFE_INTEGER = 9007199254740991L
        private const val MIN_SAFE_INTEGER = -9007199254740991L

        @JvmStatic
        val instance: BigNumberSerializer = BigNumberSerializer(Number::class.java)
    }

    @Throws(IOException::class)
    override fun serialize(value: Number, gen: JsonGenerator, provider: SerializerProvider) {
        // 超出范围 序列化为字符串否则为安全的Integer范围
        if (value.toLong() in (MIN_SAFE_INTEGER + 1)..<MAX_SAFE_INTEGER) {
            super.serialize(value, gen, provider)
        } else {
            gen.writeString(value.toString())
        }
    }

}
