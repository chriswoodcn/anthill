package cn.chriswood.anthill.example.jpa.dynamic.persistence

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EnableAspectJAutoProxy
class JpaConfigurations
