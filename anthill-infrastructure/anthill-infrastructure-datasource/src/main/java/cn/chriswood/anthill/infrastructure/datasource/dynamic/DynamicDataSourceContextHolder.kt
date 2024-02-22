package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.support.Constants
import org.slf4j.LoggerFactory


object DynamicDataSourceContextHolder {

    private val log = LoggerFactory.getLogger(javaClass)

    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private val contextHolder = ThreadLocal<String>()

    /*
     * 管理所有的数据源id;
     * 主要是为了判断数据源是否存在;
     */
    private var dataSourceTypes: List<String?> = ArrayList()

    //设置数据源
    fun setDataSourceType(dataSourceType: String) {
        log.info(">>>>>>>>>> 切换至{}数据源", dataSourceType)
        contextHolder.set(dataSourceType)
    }

    //获取数据源
    fun getDataSourceType(): String {
        println(">>>>>>>>>> 获取数据源 : " + contextHolder.get())
        return contextHolder.get() ?: Constants.PRIMARY
    }

    //清除数据源
    fun clearDataSourceType() {
        contextHolder.remove()
    }

    fun setDataSourcesTypes(list: List<String>?) {
        if (list.isNullOrEmpty()) return
        dataSourceTypes = list
    }

    fun addDataSourceType(dataSourceName: String?) {
        if (dataSourceName.isNullOrBlank()) return
        dataSourceTypes.plus(dataSourceName)
    }

    fun containsDataSourceType(dataSourceName: String?): Boolean {
        if (dataSourceName.isNullOrBlank()) return false
        return dataSourceTypes.contains(dataSourceName)
    }
}
