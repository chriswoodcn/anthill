package cn.chriswood.anthill.infrastructure.datasource.multi

import cn.chriswood.anthill.infrastructure.datasource.common.ConditionOnMultiDataSourceValid
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

@AutoConfiguration
@ConditionOnMultiDataSourceValid
class EntityManagerFactoryBuilderConfig {
    @Bean
    fun entityManagerFactoryBuilder(): EntityManagerFactoryBuilder {
        return EntityManagerFactoryBuilder(
            HibernateJpaVendorAdapter(),
            HashMap<String, String>(), null
        )
    }
}
