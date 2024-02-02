package cn.chriswood.anthill.infrastructure.datasource.multi

import cn.chriswood.anthill.infrastructure.datasource.common.ConditionOnMultiDataSourceValid
import cn.chriswood.anthill.infrastructure.datasource.common.Constants
import cn.chriswood.anthill.infrastructure.datasource.common.JpaDataSourceProperties
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@AutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
    //配置连接工厂 entityManagerFactory
    entityManagerFactoryRef = Constants.SECOND + "EntityManagerFactory",
    //配置事务管理  transactionManager
    transactionManagerRef = Constants.SECOND + "TransactionManager",
)
@ConditionalOnProperty(
    prefix = "anthill.jpa.multi.second",
    name = ["enabled"],
    havingValue = "true"
)
@EnableConfigurationProperties(JpaDataSourceProperties::class)
class JpaConfigSecond(
    var jpaProperties: JpaProperties?,
    var hibernateProperties: HibernateProperties?,
    var properties: JpaDataSourceProperties,
) {


    @Bean(name = [Constants.SECOND])
    @Primary
    fun primary(): DataSource {
        val primary = HikariDataSource()
        primary.driverClassName = properties.multi[Constants.SECOND]!!.driver
        primary.jdbcUrl = properties.multi[Constants.SECOND]!!.url
        primary.username = properties.multi[Constants.SECOND]!!.username
        primary.password = properties.multi[Constants.SECOND]!!.password
        primary.poolName = String.format("HikariPool-%s", Constants.SECOND)
        primary.isReadOnly = false
        primary.connectionTestQuery = properties.multi[Constants.SECOND]!!.query
        return primary
    }

    @Bean(name = [Constants.SECOND + "EntityManager"])
    fun entityManager(
        @Qualifier(Constants.SECOND)
        dataSource: DataSource,
        builder: EntityManagerFactoryBuilder
    ): EntityManager {
        return primaryEntityManagerFactory(dataSource, builder).getObject()!!
            .createEntityManager()
    }

    @Bean(name = [Constants.SECOND + "EntityManagerFactory"])
    fun primaryEntityManagerFactory(
        @Qualifier(Constants.SECOND)
        dataSource: DataSource,
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            //设置数据源
            .dataSource(dataSource)
            //设置数据源属性
            .properties(getHibernateProperties(dataSource))
            //设置实体类所在位置.扫描所有带有 @Entity 注解的类
            .packages(properties.multi[Constants.SECOND]!!.packageScan)
            // Spring会将EntityManagerFactory注入到Repository之中.有了 EntityManagerFactory之后,
            // Repository就能用它来创建 EntityManager 了,然后 EntityManager 就可以针对数据库执行操作
            .persistenceUnit(Constants.SECOND + "PersistenceUnit")
            .build()
    }

    fun getHibernateProperties(dataSource: DataSource): MutableMap<String, Any> {
        val map: MutableMap<String, String> = HashMap()
// 设置对应的数据库方言 hibernate 6 不需要指定方言，会自动进行判断；如果自定义Dialect class则需要指定
// Ref: https://docs.jboss.org/hibernate/orm/6.4/introduction/html_single/Hibernate_Introduction.html
// In Hibernate 6, you don’t need to specify hibernate.dialect.
// The correct Hibernate SQL Dialect will be determined for you automatically.
// The only reason to specify this property is if you’re using a custom user-written Dialect class.
        // map["hibernate.dialect"] = primaryDialect;
        if (jpaProperties == null) {
            jpaProperties = JpaProperties()
        }
        jpaProperties!!.properties = map;
        if (hibernateProperties == null) {
            hibernateProperties = HibernateProperties()
        }
        return hibernateProperties!!.determineHibernateProperties(
            jpaProperties!!.properties,
            HibernateSettings()
        )
    }

    @Bean(name = [Constants.SECOND + "TransactionManager"])
    fun transactionManager(
        @Qualifier(Constants.SECOND)
        dataSource: DataSource,
        builder: EntityManagerFactoryBuilder
    ): PlatformTransactionManager {
        return JpaTransactionManager(
            primaryEntityManagerFactory(dataSource, builder)
                .getObject()!!
        )
    }
}
