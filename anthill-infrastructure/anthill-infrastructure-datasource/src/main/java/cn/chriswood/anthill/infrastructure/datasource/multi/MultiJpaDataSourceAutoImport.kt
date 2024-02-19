package cn.chriswood.anthill.infrastructure.datasource.multi

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter


class MultiJpaDataSourceAutoImport : BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private val log = LoggerFactory.getLogger(javaClass)

    private val DATASOURCE_PREFIX = "anthill.jpa"

    private val BEAN_DATASOURCE = "%sDataSource"

    private val BEAN_ENTITY_MANAGER_FACTORY = "%sEntityManagerFactory"

    private val BEAN_TRANSACTION_MANAGER = "%sTransactionManager"

    private val PERSISTENCE_NAME = "%sPersistenceUnit"

    private val jpaProperties: Map<String, Any> = hashMapOf()

    private var environment: Environment? = null

    private var context: ApplicationContext? = null

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    }

    private fun registryDataSource(dataSourceProperty: MultiDataSourceProperty): BeanDefinition {
        return BeanDefinitionBuilder.genericBeanDefinition(
            HikariDataSource::class.java
        ) {
            if (dataSourceProperty.hikari == null)
                dataSourceProperty.hikari = HikariConfig()
            dataSourceProperty.hikari!!.jdbcUrl = dataSourceProperty.url
            dataSourceProperty.hikari!!.username = dataSourceProperty.username
            dataSourceProperty.hikari!!.password = dataSourceProperty.password
            dataSourceProperty.hikari!!.driverClassName = dataSourceProperty.driver
            dataSourceProperty.hikari!!.connectionTestQuery = dataSourceProperty.query
            HikariDataSource(dataSourceProperty.hikari);
        }.getBeanDefinition()
    }

    private fun registryEntityManagerFactory(
        name: String,
        dataSourceProperty: MultiDataSourceProperty,
        beanFactory: DefaultListableBeanFactory
    ): BeanDefinition {
        val hikariDataSource = beanFactory.getBean(
            String.format(BEAN_DATASOURCE, name),
            HikariDataSource::class.java
        )
//         jpaProperties.plus("hibernate.dialect" to dataSourceProperty.dialect)
        return BeanDefinitionBuilder.genericBeanDefinition<LocalContainerEntityManagerFactoryBean>(
            LocalContainerEntityManagerFactoryBean::class.java
        ) {
            val entityManagerFactoryBean =
                LocalContainerEntityManagerFactoryBean()
            entityManagerFactoryBean.setDataSource(hikariDataSource)
            entityManagerFactoryBean.setPackagesToScan(dataSourceProperty.packageScan)
            entityManagerFactoryBean.jpaVendorAdapter = HibernateJpaVendorAdapter()
            jpaProperties.plus("hibernate.dialect" to dataSourceProperty.dialect)
            entityManagerFactoryBean.setJpaPropertyMap(jpaProperties)
            entityManagerFactoryBean.setPersistenceUnitName(String.format(PERSISTENCE_NAME, name))
            entityManagerFactoryBean
        }.getBeanDefinition()
    }

    private fun registryTransactionManager(name: String, beanFactory: DefaultListableBeanFactory): BeanDefinition {
        val builder = BeanDefinitionBuilder.rootBeanDefinition(JpaTransactionManager::class.java)
        builder.addConstructorArgReference(String.format(BEAN_ENTITY_MANAGER_FACTORY, name))
        return builder.getBeanDefinition()
    }

    //    postProcessBeanFactory
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val defaultListableBeanFactory = beanFactory as DefaultListableBeanFactory
        val multipleDataSourceProperties = Binder.get(environment)
            .bindOrCreate(DATASOURCE_PREFIX, MultiDataSourceProperties::class.java)
        if (multipleDataSourceProperties.multi.isNotEmpty()) {
            multipleDataSourceProperties.validate()
            multipleDataSourceProperties.multi.forEach {
                log.info("注册数据源[{}]", it.key)
                // 注册数据源
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_DATASOURCE, it.key),
                    registryDataSource(it.value)
                )
                // 注册entityManagerFactory
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_ENTITY_MANAGER_FACTORY, it.key),
                    registryEntityManagerFactory(it.key, it.value, defaultListableBeanFactory)
                )
                // 注册事务管理器
                defaultListableBeanFactory.registerBeanDefinition(
                    String.format(BEAN_TRANSACTION_MANAGER, it.key),
                    registryTransactionManager(it.key, defaultListableBeanFactory)
                )
            }
        }
    }
}
