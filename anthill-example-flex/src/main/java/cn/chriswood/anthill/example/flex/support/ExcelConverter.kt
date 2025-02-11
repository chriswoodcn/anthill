package cn.chriswood.anthill.example.flex.support

import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil
import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import com.alibaba.excel.converters.Converter
import com.alibaba.excel.converters.WriteConverterContext
import com.alibaba.excel.metadata.data.WriteCellData


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ExcelHead(
    //excel head简单翻译 eg:zh@ID;
    val value: String
)

enum class ExcelDictType(val code: String) {
    INNER("InnerDict"), DATASOURCE("")
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ExcelDict(
    //字典的键
    val value: String, val type: ExcelDictType = ExcelDictType.INNER
)

class ExcelDictConverter : Converter<Any> {
    override fun convertToExcelData(context: WriteConverterContext<Any>?): WriteCellData<*> {
        if (context == null) return WriteCellData<Any>()
        val excelDict = (context.contentProperty.field.annotations.find {
            it.annotationClass == ExcelDict::class
        } ?: return super.convertToExcelData(context)) as ExcelDict
        val dictType = excelDict.type
        val locale = HttpRequestUtil.getLocale()
        return when (dictType) {
            ExcelDictType.INNER -> {
                val dictAlias =
                    "${dictType.code}.${excelDict.value}.${context.value}"
                WriteCellData<Any>(
                    I18nMessageUtil.messageByLocale(locale, dictAlias)
                        ?: dictAlias
                )
            }

            ExcelDictType.DATASOURCE -> {
                WriteCellData<Any>()
                //TODO 这里使用数据库的字典
            }
        }
    }
}

