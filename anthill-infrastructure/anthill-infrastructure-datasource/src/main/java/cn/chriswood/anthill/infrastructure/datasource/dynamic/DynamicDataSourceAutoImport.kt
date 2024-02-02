package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.DataSourceTypeEnum
import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
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
import java.util.function.Supplier
import javax.sql.DataSource

class DynamicDataSourceAutoImport :
    ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa.dynamic"

    //存储注册的数据源
    private val dynamicDataSources: MutableMap<String, DataSource> = mutableMapOf()

    private var environment: Environment? = null

    private var context: ApplicationContext? = null

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
    private fun createDataSource(dataSourceProperty: JpaDataSourceProperty): HikariDataSource {
        if (dataSourceProperty.hikari == null)
            dataSourceProperty.hikari = HikariConfig()
        dataSourceProperty.hikari!!.jdbcUrl = dataSourceProperty.url
        dataSourceProperty.hikari!!.username = dataSourceProperty.username
        dataSourceProperty.hikari!!.password = dataSourceProperty.password
        dataSourceProperty.hikari!!.driverClassName = dataSourceProperty.driver
        dataSourceProperty.hikari!!.connectionTestQuery = dataSourceProperty.query
        return HikariDataSource(dataSourceProperty.hikari)
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val binder = Binder.get(environment)
        val dataSourceType = binder.bindOrCreate(Constants.DATASOURCE_TYPE, Bindable.of(String::class.java))
        if (dataSourceType != DataSourceTypeEnum.DynamicJPA.code) return
        val dataSourceProperties: MutableMap<String, JpaDataSourceProperty>? =
            binder.bindOrCreate(
                DATASOURCE_PREFIX,
                Bindable.mapOf(String::class.java, JpaDataSourceProperty::class.java)
            )

        if (!dataSourceProperties.isNullOrEmpty()) {
            dataSourceProperties.entries.forEach {
                log.info(">>>>>>>>>> 动态数据源配置[{}]", it.key)
                // 校验数据源参数
                if (!it.value.validate()) return@forEach
                val hikariDataSource = createDataSource(it.value)
                log.info(">>>>>>>>>> hikariDataSource:{}", hikariDataSource)
                dynamicDataSources[it.key] = hikariDataSource
            }
            DynamicDataSourceContextHolder.setDataSourcesTypes(dataSourceProperties.keys.toList())
            DynamicDataSourceContextHolder.setDataSourceType(Constants.PRIMARY)
            log.info(">>>>>>>>>> 一共生成{}个数据源", dynamicDataSources.size)
            registryDynamicDataSource(dynamicDataSources)
        }
    }

    private fun registryDynamicDataSource(dynamicDataSources: Map<String, DataSource>): BeanDefinition {
        return BeanDefinitionBuilder.genericBeanDefinition(
            DynamicDataSource::class.java,
            Supplier {
                val dynamicDataSource = DynamicDataSource()
                dynamicDataSource.setDefaultTargetDataSource(Constants.PRIMARY)
                dynamicDataSource.setTargetDataSources(dynamicDataSources as Map<Any, Any>)
                dynamicDataSource
            }).getBeanDefinition()
    }
}
