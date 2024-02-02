package cn.chriswood.anthill.infrastructure.datasource.multi

import cn.chriswood.anthill.infrastructure.datasource.DataSourceTypeEnum
import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperty
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
import java.util.function.Supplier

class MultiJpaDataSourceAutoImport : BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private var jpaProperties: JpaProperties? = null

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa.multi"

    private val BEAN_DATASOURCE = "%sDataSource"

    private val BEAN_ENTITY_MANAGER_FACTORY = "%sEntityManagerFactory"

    private val BEAN_ENTITY_MANAGER = "%sEntityManager"

    private val BEAN_TRANSACTION_MANAGER = "%sTransactionManager"

    private val PERSISTENCE_NAME = "%sPersistenceUnit"

    private var environment: Environment? = null

    private var context: ApplicationContext? = null
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    }

    /**
     * 注册Hikari数据源
     * @param dataSourceProperty  数据源配置信息
     */
    private fun registryDataSource(dataSourceProperty: JpaDataSourceProperty): BeanDefinition {
        return BeanDefinitionBuilder.genericBeanDefinition(
            HikariDataSource::class.java,
            Supplier {
                if (dataSourceProperty.hikari == null)
                    dataSourceProperty.hikari = HikariConfig()
                dataSourceProperty.hikari!!.jdbcUrl = dataSourceProperty.url
                dataSourceProperty.hikari!!.username = dataSourceProperty.username
                dataSourceProperty.hikari!!.password = dataSourceProperty.password
                dataSourceProperty.hikari!!.driverClassName = dataSourceProperty.driver
                dataSourceProperty.hikari!!.connectionTestQuery = dataSourceProperty.query
                HikariDataSource(dataSourceProperty.hikari)
            }).getBeanDefinition()
    }

    /**
     * 注册EntityManagerFactory
     * @param name          数据源名称
     * @param dataSource    数据源配置信息
     * @param beanFactory
     */
    private fun registryEntityManagerFactory(
        name: String,
        dataSourceProperty: JpaDataSourceProperty,
        beanFactory: DefaultListableBeanFactory
    ): BeanDefinition {
        val hikariDataSource = beanFactory.getBean(
            String.format(BEAN_DATASOURCE, name),
            HikariDataSource::class.java
        )
        val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(
            LocalContainerEntityManagerFactoryBean::class.java,
            Supplier {
                val entityManagerFactoryBean =
                    LocalContainerEntityManagerFactoryBean()
                entityManagerFactoryBean.setDataSource(hikariDataSource)
                entityManagerFactoryBean.setPackagesToScan(dataSourceProperty.packageScan)
                entityManagerFactoryBean.jpaVendorAdapter = HibernateJpaVendorAdapter()
                entityManagerFactoryBean.setJpaPropertyMap(jpaProperties?.properties)
                entityManagerFactoryBean.setPersistenceUnitName(String.format(PERSISTENCE_NAME, name))
                entityManagerFactoryBean
            }).getBeanDefinition()
        return beanDefinition
    }

    private fun registryEntityManager(
        name: String,
        beanFactory: DefaultListableBeanFactory
    ): BeanDefinition {
        val factoryBean = beanFactory.getBean(
            String.format(BEAN_ENTITY_MANAGER_FACTORY, name),
            LocalContainerEntityManagerFactoryBean::class.java
        )
        return BeanDefinitionBuilder.genericBeanDefinition(
            EntityManager::class.java,
            Supplier {
                factoryBean.`object`?.createEntityManager()
            }).getBeanDefinition()
    }

    /**
     * 注册数据源事务管理器
     * @param name
     * @param beanFactory
     */
    private fun registryTransactionManager(name: String, beanFactory: DefaultListableBeanFactory): BeanDefinition {
        val builder: BeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(JpaTransactionManager::class.java)
        builder.addConstructorArgReference(String.format(BEAN_ENTITY_MANAGER_FACTORY, name))
        return builder.getBeanDefinition()
    }


    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val defaultListableBeanFactory = beanFactory as DefaultListableBeanFactory
        val binder = Binder.get(environment)
        val dataSourceType = binder.bindOrCreate(Constants.DATASOURCE_TYPE, Bindable.of(String::class.java))
        if (dataSourceType != DataSourceTypeEnum.MultiJPA.code) return
        val dataSourceProperties: MutableMap<String, JpaDataSourceProperty>? =
            binder.bindOrCreate(
                DATASOURCE_PREFIX,
                Bindable.mapOf(String::class.java, JpaDataSourceProperty::class.java)
            )
        jpaProperties = binder.bindOrCreate("spring.jpa", Bindable.of(JpaProperties::class.java))
        if (!dataSourceProperties.isNullOrEmpty()) {
            dataSourceProperties.entries.forEach {
                log.debug(">>>>>>>>>> [anthill-infrastructure-datasource] 多数据源配置[{}]", it.key)
                // 校验数据源参数
                if (!it.value.validateWithPackage()) return@forEach
                // 注册数据源
                log.debug(">>>>>>>>>> 注册{}DataSource", it.key)
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_DATASOURCE, it.key),
                    registryDataSource(it.value)
                )
                log.debug(">>>>>>>>>> 注册{}EntityManagerFactory", it.key)
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_ENTITY_MANAGER_FACTORY, it.key),
                    registryEntityManagerFactory(
                        it.key, it.value,
                        defaultListableBeanFactory
                    )
                )
                log.debug(">>>>>>>>>> 注册{}EntityManager", it.key)
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_ENTITY_MANAGER, it.key),
                    registryEntityManager(
                        it.key,
                        defaultListableBeanFactory
                    )
                )
                log.debug(">>>>>>>>>> 注册{}TransactionManager", it.key)
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_TRANSACTION_MANAGER, it.key),
                    registryTransactionManager(
                        it.key,
                        defaultListableBeanFactory
                    )
                )
            }
        }
    }
}
