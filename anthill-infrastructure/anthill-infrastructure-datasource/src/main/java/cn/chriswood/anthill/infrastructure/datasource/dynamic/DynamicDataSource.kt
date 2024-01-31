package cn.chriswood.anthill.infrastructure.datasource.dynamic

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class DynamicDataSource : AbstractRoutingDataSource() {
    /**
     * determineCurrentLookupKey方法取得一个字符串，
     * 该字符串将与配置文件中的相应字符串进行匹配以定位数据源
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     * 确定当前查找关键字。这通常用于检查线程绑定的事务上下文。
     * 允许使用任意键。返回的键需要与存储的查找键类型匹配，如resolveSpecifiedLookupKey方法所解析的
     */
    override fun determineCurrentLookupKey(): Any {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
