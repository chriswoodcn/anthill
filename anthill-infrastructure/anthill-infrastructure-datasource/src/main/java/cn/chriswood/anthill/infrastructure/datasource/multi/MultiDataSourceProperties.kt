package cn.chriswood.anthill.infrastructure.datasource.multi
data class MultiDataSourceProperties(
    val multi: Map<String, MultiDataSourceProperty>
) {
    /**
     * 数据源配置参数校验
     */
    fun validate() {
        multi.values.forEach { it.validate() }
    }
}
