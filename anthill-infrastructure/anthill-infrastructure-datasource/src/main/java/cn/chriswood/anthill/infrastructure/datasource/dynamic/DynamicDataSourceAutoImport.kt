package cn.chriswood.anthill.infrastructure.datasource.dynamic

import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import java.util.*
import javax.sql.DataSource

class DynamicDataSourceAutoImport :
    ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa.dynamic"

    //存储注册的数据源
    private val dynamicDataSources: Map<String, DataSource> = HashMap<String, DataSource>()

    private var environment: Environment? = null

    private var context: ApplicationContext? = null

    private val aliases = ConfigurationPropertyNameAliases()

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
        val dataSourceProperties: DynamicDataSourceProperties = Binder.get(environment)
            .bind(DATASOURCE_PREFIX, DynamicDataSourceProperties::class.java).get()

        if (Optional.ofNullable<Any>(dataSourceProperties).isPresent
            && dataSourceProperties.dataSources.isNotEmpty()
        ) {
            dataSourceProperties.dataSources.entries.forEach {
                log.info(">>>>>>>>>> 动态数据源配置[{}]", it.key)
                // 校验数据源参数
                if (it.value == null) return@forEach
                if (!it.value!!.validate()) return@forEach
                val hikariDataSource = createDataSource(it.value!!)
                dynamicDataSources.plus(it.key to hikariDataSource)
            }
            log.info(">>>>>>>>>> 一共注册{}个数据源", dynamicDataSources.size)
        }
    }
}
