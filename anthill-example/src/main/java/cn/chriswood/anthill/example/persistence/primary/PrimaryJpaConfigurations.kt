package cn.chriswood.anthill.example.persistence.primary

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["cn.chriswood.anthill.example.persistence.primary"],
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
class PrimaryJpaConfigurations
