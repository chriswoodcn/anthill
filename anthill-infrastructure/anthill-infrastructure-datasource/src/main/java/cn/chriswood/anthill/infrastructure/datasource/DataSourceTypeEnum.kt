package cn.chriswood.anthill.infrastructure.datasource

import cn.chriswood.anthill.infrastructure.datasource.common.Constants

enum class DataSourceTypeEnum(var code: String) {
    MultiJPA(Constants.MULTI),
    DynamicJPA(Constants.DYNAMIC);

    companion object {
        fun getDataSourceTypeByCode(code: String): DataSourceTypeEnum {
            var enum = DataSourceTypeEnum.MultiJPA
            entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
