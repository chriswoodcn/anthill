package cn.chriswood.anthill.example.flex.support

import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import cn.chriswood.anthill.infrastructure.core.utils.I18nMessageUtil
import cn.chriswood.anthill.infrastructure.web.utils.HttpRequestUtil
import cn.hutool.core.util.StrUtil
import com.alibaba.excel.annotation.ExcelProperty
import com.mybatisflex.core.constant.SqlOperator
import com.mybatisflex.core.query.QueryColumn
import com.mybatisflex.core.query.QueryCondition
import com.mybatisflex.core.row.Row
import com.mybatisflex.core.row.RowKey
import com.mybatisflex.core.table.EntityMetaObject
import com.mybatisflex.core.table.TableInfo
import com.mybatisflex.core.table.TableInfoFactory
import com.mybatisflex.core.util.StringUtil
import com.mybatisflex.kotlin.extensions.db.tableInfo
import org.apache.logging.log4j.util.Strings
import java.util.*
import kotlin.reflect.KProperty1


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FlexQueryColumn(
    val value: SqlOperator = SqlOperator.EQUALS,
    val target: String = Strings.EMPTY,
)

/**
 * 根据BO对象生成QueryCondition
 */
inline fun <reified T : Any> buildQueryConditionsByBo(
    bo: T?,
    tableInfo: TableInfo
): QueryCondition {
    val queryCondition = QueryCondition.createEmpty()
    if (bo == null) return queryCondition
    //获取表信息
    val camelToUnderline = tableInfo.isCamelToUnderline
    val tableName = tableInfo.tableName
    val queryColumns = tableInfo.defaultQueryColumn
    tableInfo.defaultQueryColumns
    val fields = BeanUtil.getAllFields(T::class)
    val filterProperties = fields.filter {
        it.isAccessible = true
        it.isAnnotationPresent(FlexQueryColumn::class.java) && it.get(bo) != null
    }
    //组合QueryCondition
    filterProperties.forEach { p ->
        val value = p.get(bo)!!
        val operator =
            (p.annotations.find { anno -> anno.annotationClass == FlexQueryColumn::class }
                ?: return@forEach) as FlexQueryColumn
        val queryColumn = queryColumns.find { qc ->
            val convertName = if (camelToUnderline) {
                StringUtil.camelToUnderline(p.name)
            } else {
                p.name
            }
            qc.name == convertName
        }
        if (queryColumn != null) {
            //匹配到queryColumn相同字段
            val condition =
                QueryCondition.create(queryColumn, operator.value, value)
            queryCondition.and(condition)
        } else {
            //没有匹配到queryColumn相同字段
            if (operator.target.isNotBlank()) {
                val convertName = if (camelToUnderline) {
                    StringUtil.camelToUnderline(operator.target)
                } else {
                    operator.target
                }
                val qc = QueryColumn(
                    tableName,
                    convertName
                )
                val condition = QueryCondition.create(qc, operator.value, value)
                queryCondition.and(condition)
            }
        }
    }
    return queryCondition
}

inline fun <reified T : Any> buildRow(
    property: KProperty1<T, *>,
    value: Any
): Row {
    val camelToUnderline = T::class.tableInfo.isCamelToUnderline
    return if (camelToUnderline) {
        Row.of(StringUtil.camelToUnderline(property.name), value)
    } else {
        Row.of(property.name, value)
    }
}

fun <T : Any> T.toRowWithPrimaryKeys(): Row {
    val tableInfo: TableInfo = TableInfoFactory.ofEntityClass(javaClass)
    val metaObject =
        EntityMetaObject.forObject(this, tableInfo.reflectorFactory)
    val rowKeys = tableInfo.primaryKeyList.map {
        RowKey.of(it.column, it.keyType)
    }.toTypedArray()
    val row = Row.ofKey(*rowKeys)
    tableInfo.primaryKeyList.forEach { idInfo ->
        metaObject.getValue(idInfo.property)
            ?.let { row[idInfo.column] = it }
    }
    tableInfo.columnInfoList.forEach { idInfo ->
        metaObject.getValue(idInfo.property)
            ?.let { row[idInfo.column] = it }
    }
    return row
}


inline fun <reified T : Any> genI18nExcelHead(
    prefix: String? = null
): List<List<String>> {
    val clzName = T::class.java.simpleName
    val declaredFields = T::class.java.declaredFields
    val fields = declaredFields.filter {
        it.isAccessible = true
        val annotations = it.annotations
        annotations.map { an -> an.annotationClass }
            .contains(ExcelProperty::class)
    }
    val sortedField = fields.mapNotNull {
        val property =
            it.annotations.find { an -> an.annotationClass == ExcelProperty::class }
                ?: return@mapNotNull null
        it.name to property
    }.sortedBy {
        val second = it.second as ExcelProperty
        +second.index.and(+second.order)
    }
    val finalPrefix = prefix ?: "ExcelHead"
    val finalLocale = getLocale()
    val max = sortedField.maxOfOrNull {
        val second = it.second as ExcelProperty
        // 因ExcelProperty的value默认为空串,需要去掉
        if (second.value.size == 1 &&
            second.value[0] == Strings.EMPTY
        ) {
            0
        } else {
            second.value.size
        }
    } ?: 0
    val list = MutableList<List<String>>(sortedField.size) {
        mutableListOf()
    }
    sortedField.forEachIndexed { idx, p ->
        list[idx] = (0..max).map {
            if (it == max) {
                try {
                    I18nMessageUtil.messageByLocale(
                        finalLocale,
                        "${finalPrefix}.${clzName}.${p.first}"
                    ) ?: StrUtil.upperFirst(p.first)
                } catch (e: Exception) {
                    StrUtil.upperFirst(p.first)
                }
            } else {
                val second = p.second as ExcelProperty
                val size = second.value.size
                if (size >= it) {
                    second.value[it]
                } else {
                    Strings.EMPTY
                }
            }
        }
    }
    return list
}

fun getLocale(): Locale {
    return try {
        HttpRequestUtil.getLocale()
    } catch (e: Exception) {
        Locale.getDefault()
    }
}
