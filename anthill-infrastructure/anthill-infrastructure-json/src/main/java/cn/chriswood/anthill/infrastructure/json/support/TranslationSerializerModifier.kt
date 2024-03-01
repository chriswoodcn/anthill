package cn.chriswood.anthill.infrastructure.json.support

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier

class TranslationSerializerModifier : BeanSerializerModifier() {
    override fun changeProperties(
        config: SerializationConfig?,
        beanDesc: BeanDescription?,
        beanProperties: MutableList<BeanPropertyWriter>?
    ): MutableList<BeanPropertyWriter>? {
        if (beanProperties != null) {
            for (writer in beanProperties) {
                // 如果序列化器为 TranslationHandler 的话 将 Null 值也交给他处理
                if (writer.serializer is TranslateSerializer) {
                    writer.assignNullSerializer(writer.serializer)
                }
            }
        }
        return beanProperties
    }
}
