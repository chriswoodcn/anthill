package cn.chriswood.anthill.infrastructure.jpa.multi

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

class MultiDataSourceAutoImport : ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa"

    private val BEAN_DATASOURCE = "%sDataSource"

    private val BEAN_ENTITY_MANAGER_FACTORY = "%sEntityManagerFactory"

    private val BEAN_TRANSACTION_MANAGER = "%sTransactionManager"

    private val PERSISTENCE_NAME = "%sPersistenceUnit"

    private var environment: Environment? = null

    private var context: ApplicationContext? = null
    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        log.debug(">>>>>>>>>> init jpa多数据源配置 >>>>>>>>>>")
        val multipleDataSourceProperties =
            Binder.get(environment).bindOrCreate(DATASOURCE_PREFIX, MultiDataSourceProperties::class.java)
        if (multipleDataSourceProperties.multi.isNotEmpty()) {
            multipleDataSourceProperties.validate()
            multipleDataSourceProperties.multi.forEach {
                log.debug("注册{}DataSource", it.key)
                registerDataSource(it.key, it.value, registry)
                log.debug("注册{}EntityManagerFactory", it.key)
                registerEntityManagerFactory(it.key, it.value, registry)
                log.debug("注册{}TransactionManager", it.key)
                registerTransactionManager(it.key, registry)
            }
        }
    }

    private fun registerTransactionManager(
        name: String, registry: BeanDefinitionRegistry
    ) {
        val beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(JpaTransactionManager::class.java)
            .addConstructorArgReference(String.format(BEAN_ENTITY_MANAGER_FACTORY, name)).getBeanDefinition()
        registry.registerBeanDefinition(String.format(BEAN_TRANSACTION_MANAGER, name), beanDefinition)
    }

    private fun registerEntityManagerFactory(
        name: String, dataSourceProperty: MultiDataSourceProperty, registry: BeanDefinitionRegistry
    ) {
        val beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(
            LocalContainerEntityManagerFactoryBean::class.java
        ) {
            val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
            val hikariDataSource =
                context?.getBean<HikariDataSource>(String.format(BEAN_DATASOURCE, name)) ?: HikariDataSource(
                    dataSourceProperty.hikari
                )
            entityManagerFactoryBean.setDataSource(hikariDataSource)
            entityManagerFactoryBean.setPackagesToScan(dataSourceProperty.packageScan)
            entityManagerFactoryBean.jpaVendorAdapter = HibernateJpaVendorAdapter()
            val hibernateProperties =
                context?.getBean<HibernateProperties>("hibernateProperties") ?: HibernateProperties()
            val jpaProperties = Binder.get(environment).bindOrCreate("spring.jpa", JpaProperties::class.java)
            log.debug(">>>>>>>>>> merge jpaProperties.properties: {}", jpaProperties.properties.toString())
            val properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings()
            )
            entityManagerFactoryBean.setJpaPropertyMap(properties)
            entityManagerFactoryBean.setPersistenceUnitName(String.format(PERSISTENCE_NAME, name))
            entityManagerFactoryBean
        }.addDependsOn(String.format(BEAN_DATASOURCE, name)).getBeanDefinition()
        registry.registerBeanDefinition(String.format(BEAN_ENTITY_MANAGER_FACTORY, name), beanDefinition)
    }

    private fun registerDataSource(
        name: String, dataSourceProperty: MultiDataSourceProperty, registry: BeanDefinitionRegistry
    ) {
        val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(
            HikariDataSource::class.java
        ) {
            dataSourceProperty.hikari.jdbcUrl = dataSourceProperty.url
            dataSourceProperty.hikari.username = dataSourceProperty.username
            dataSourceProperty.hikari.password = dataSourceProperty.password
            dataSourceProperty.hikari.driverClassName = dataSourceProperty.driver
            dataSourceProperty.hikari.connectionTestQuery = dataSourceProperty.query
            HikariDataSource(dataSourceProperty.hikari);
        }.getBeanDefinition()
        registry.registerBeanDefinition(String.format(BEAN_DATASOURCE, name), beanDefinition)
    }
}
