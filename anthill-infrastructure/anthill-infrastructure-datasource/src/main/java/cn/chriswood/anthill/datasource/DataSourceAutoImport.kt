package cn.chriswood.anthill.datasource

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment


@EnableConfigurationProperties(DataSourceProperties::class)
class DataSourceAutoImport(
    val dataSourceProperties: DataSourceProperties
) : ImportBeanDefinitionRegistrar, EnvironmentAware {
    override fun setEnvironment(environment: Environment) {
//        try {
//            val resolver = RelaxedPropertyResolver(environment)
//            val properties: Map<String, DataSourceProperties.DataSourceProperty?> = resolver.getSubProperties("")
//            val beanProperties = DataSourceProperties::class.java.newInstance()
//            val binder = RelaxedDataBinder(
//                beanProperties, getPropertiesPrefix(
//                    RateLimiterConfigBeanProperties::class.java
//                )
//            )
//            binder.bind(MutablePropertyValues(properties))
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
    }
}
