package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.DataSourceTypeEnum
import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import javax.sql.DataSource

class DynamicDataSourceAutoImport : ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa.dynamic"

    //存储注册的数据源
    private val dynamicDataSources: MutableMap<String, DataSource> = mutableMapOf()

    private var environment: Environment? = null

    private var context: ApplicationContext? = null

    private var defaultTargetDataSource: DataSource? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    /**
     * 注册Hikari数据源
     * @param dataSourceProperty  数据源配置信息
     */
    private fun createDataSource(dataSourceProperty: DynamicDataSourceProperty): HikariDataSource {
        dataSourceProperty.hikari.jdbcUrl = dataSourceProperty.url
        dataSourceProperty.hikari.username = dataSourceProperty.username
        dataSourceProperty.hikari.password = dataSourceProperty.password
        dataSourceProperty.hikari.driverClassName = dataSourceProperty.driver
        dataSourceProperty.hikari.connectionTestQuery = dataSourceProperty.query
        return HikariDataSource(dataSourceProperty.hikari)
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        log.debug(">>>>>>>>>> init jpa动态数据源配置 >>>>>>>>>>")
        val binder = Binder.get(environment)
        val dataSourceType = binder.bindOrCreate(Constants.DATASOURCE_TYPE, Bindable.of(String::class.java))
        if (dataSourceType != DataSourceTypeEnum.DynamicJPA.code) return
        val dataSourceProperties: MutableMap<String, DynamicDataSourceProperty>? = binder.bindOrCreate(
            DATASOURCE_PREFIX, Bindable.mapOf(String::class.java, DynamicDataSourceProperty::class.java)
        )

        if (!dataSourceProperties.isNullOrEmpty()) {
            dataSourceProperties.entries.forEach {
                log.debug(">>>>>>>>>> 创建数据源{}", it.key)
                // 校验数据源参数
                if (!it.value.validate()) return@forEach
                val hikariDataSource = createDataSource(it.value)
                dynamicDataSources[it.key] = hikariDataSource
                if (Constants.PRIMARY == it.key) defaultTargetDataSource = hikariDataSource
            }
            log.debug(">>>>>>>>>> 一共创建{}个数据源", dynamicDataSources.size)
            // bean定义类
            val dynamicDataSourceBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource::class.java) {
                    val dynamicDataSource = DynamicDataSource()
                    dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource!!)
                    dynamicDataSource.setTargetDataSources(dynamicDataSources as Map<Any, Any>)
                    dynamicDataSource
                }.getBeanDefinition()
            registry.registerBeanDefinition("datasource", dynamicDataSourceBeanDefinition)
            DynamicDataSourceContextHolder.setDataSourcesTypes(dataSourceProperties.keys.toList())
        }
    }
}
