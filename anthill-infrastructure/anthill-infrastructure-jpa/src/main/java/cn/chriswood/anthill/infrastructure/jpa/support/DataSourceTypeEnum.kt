package cn.chriswood.anthill.infrastructure.jpa.support

enum class DataSourceTypeEnum(var code: String) {
    MultiJPA(Constants.MULTI),
    DynamicJPA(Constants.DYNAMIC);

    companion object {
        fun getDataSourceTypeByCode(code: String): DataSourceTypeEnum {
            var enum = MultiJPA
            entries.forEach {
                if (code == it.code) enum = it
            }
            return enum
        }
    }
}
