package cn.chriswood.anthill.example.flex.store

import cn.chriswood.anthill.example.flex.support.buildQueryConditionsByBo
import cn.chriswood.anthill.example.flex.support.buildRow
import cn.chriswood.anthill.example.flex.support.toRowWithPrimaryKeys
import cn.chriswood.anthill.infrastructure.core.utils.BeanUtil
import com.mybatisflex.core.constant.SqlOperator
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryColumn
import com.mybatisflex.core.query.QueryCondition
import com.mybatisflex.core.query.QueryOrderBy
import com.mybatisflex.core.row.Db
import com.mybatisflex.kotlin.extensions.db.paginate
import com.mybatisflex.kotlin.extensions.db.query
import com.mybatisflex.kotlin.extensions.db.tableInfo
import com.mybatisflex.kotlin.extensions.model.toRow
import com.mybatisflex.kotlin.extensions.wrapper.whereWith
import java.io.Serializable
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

abstract class BaseStorage<T : Any, B : Any, D : Any> {
    inline fun <reified T : Any> convert2Data(dto: D): T {
        return BeanUtil.copyBean(dto)
    }

    inline fun <reified D : Any> getById(id: Serializable): D? {
        return D::class.tableInfo.let {
            Db.selectOneById(it.tableName, it.primaryColumns[0], id)
        }.toEntity(D::class.java)
    }

    inline fun <reified B : Any, reified D : Any> page(
        pageNum: Int,
        pageSize: Int,
        bo: B?,
        vararg orderBys: QueryOrderBy
    ): Page<D> {
        return paginate<D>(
            Page.of(pageNum, pageSize)
        ) {
            select()
            whereWith {
                buildQueryConditionsByBo<B>(bo, D::class.tableInfo)
            }
            if (orderBys.isNotEmpty()) {
                orderBy(*orderBys)
            }
        }
    }

    inline fun <reified B : Any, reified D : Any>
        buildCondition(
        bo: B?,
        extraCondition: QueryCondition? = null
    ): QueryCondition {
        val condition = buildQueryConditionsByBo<B>(
            bo,
            D::class.tableInfo
        )
        return if (extraCondition !== null) condition.and(extraCondition)
        else condition
    }

    inline fun <reified B : Any, reified D : Any> list(
        bo: B?,
        vararg orderBys: QueryOrderBy
    ): List<D> {
        return query<D> {
            select()
            whereWith {
                buildCondition<B, D>(bo)
            }
            if (orderBys.isNotEmpty()) {
                orderBy(*orderBys)
            }
        }
    }

    inline fun <reified B : Any, reified D : Any>
        count(bo: B?, excludes: List<Serializable>): Long {
        val condition =
            buildQueryConditionsByBo<B>(bo, D::class.tableInfo)
        return D::class.tableInfo.let {
            Db.selectCountByCondition(
                it.tableName,
                if (excludes.isNotEmpty())
                    condition.and(
                        QueryCondition.create(
                            QueryColumn(it.tableName, it.primaryColumns[0]),
                            SqlOperator.NOT_IN, excludes
                        )
                    )
                else condition
            )
        }
    }

    inline fun <reified B : Any, reified D : Any>
        add(bo: B): Serializable? {
        val dto = BeanUtil.copyBean<D>(bo)
        val row = dto.toRowWithPrimaryKeys()
        return D::class.tableInfo.let {
            val r = Db.insert(it.tableName, row)
            if (r > 0) {
                row[it.primaryColumns[0]] as Serializable
            } else {
                null
            }
        }
    }

    inline fun <reified B : Any, reified D : Any>
        update(bo: B): Boolean {
        val dto = BeanUtil.copyBean<D>(bo)
        return D::class.tableInfo.let {
            Db.updateById(it.tableName, dto.toRowWithPrimaryKeys())
        } > 0
    }

    inline fun <reified D : Any>
        remove(
        id: Serializable,
        logicColumn: KMutableProperty1<D, Any>,
        logicValue: Serializable
    ): Boolean {
        val dto = getById<D>(id) ?: return true
        if (dto::class.memberProperties.contains(logicColumn)) {
            logicColumn.set(dto, logicValue)
            return Db.updateById(
                D::class.tableInfo.tableName,
                dto.toRow()
            ) > 0
        } else {
            return false
        }
    }

    inline fun <reified D : Any>
        batchRemove(
        ids: List<Serializable>,
        logicColumn: KMutableProperty1<D, *>,
        logicValue: Serializable
    ): Boolean {
        val row = buildRow(logicColumn, logicValue)
        val qc = QueryColumn(
            D::class.tableInfo.tableName,
            D::class.tableInfo.primaryColumns[0]
        )
        val condition = QueryCondition.create(qc, SqlOperator.IN, ids)
        val count = Db.selectCountByCondition(
            D::class.tableInfo.tableName,
            condition
        )
        return Db.updateByCondition(
            D::class.tableInfo.tableName,
            row,
            condition
        ) == count.toInt()
    }

    inline fun <reified D : Any>
        delete(id: Serializable): Boolean {
        return D::class.tableInfo.let {
            Db.deleteById(
                it.tableName,
                it.primaryColumns[0],
                id
            )
        } > 0
    }

    inline fun <reified T : Any>
        batchDelete(ids: Collection<Serializable>): Boolean {
        return Db.tx {
            T::class.tableInfo.let {
                Db.deleteBatchByIds(
                    it.tableName,
                    it.primaryColumns[0],
                    ids
                )
            } == ids.size
        }
    }
}
