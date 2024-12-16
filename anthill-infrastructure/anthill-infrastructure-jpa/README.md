# anthill-infrastructure-datasource

提供框架jpa动态数据源和多数据源配置支持

## Configurations

~~~yml
---
anthill:
  jpa:
    enabled: true #开启jpa
    type: dynamic #动态数据源
    dynamic:
      primary: #主数据源必须primary
        driver: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.1.188:3306/tec?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
        username: root
        password: rootroot
        query: SELECT 1 FROM DUAL
      slave:
        driver: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/scootor_mt?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B2
        username: root
        password: rootroot
        query: SELECT 1 FROM DUAL

#使用动态数据源  还需在应用主入口加上下面的注解
#@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class]) 排除自动数据源的注入
#@EnableJpaRepositories  开启jpa自动配置repository工厂
#@EnableAspectJAutoProxy  开启aop切面

---
anthill:
  jpa:
    type: multi #多数据源
    multi:
      primary:
        driver: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://192.168.1.188:3306/tec?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
        username: root
        password: rootroot
        query: SELECT 1 FROM DUAL
        dialect: org.hibernate.dialect.MySQLDialect  #多数据源需要配置数据库方言
        package-scan: cn.chriswood.anthill.example.persistence.primary  #多数据源需要制定扫描包名
      second:
        driver: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/scootor_mt?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B2
        username: root
        password: rootroot
        query: SELECT 1 FROM DUAL
        dialect: org.hibernate.dialect.MySQLDialect  #多数据源需要配置数据库方言
        package-scan: cn.chriswood.anthill.example.persistence.secondarydary  #多数据源需要制定扫描包名

#使用多数据源  还需在应用主入口加上下面的注解
#@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class]) 排除自动数据源的注入

#使用多数据源  还需在对应的包下指定jpa的 entityManagerFactoryRef和transactionManagerRef
#以second数据源为例,默认会注入secondEntityManagerFactory secondTransactionManager  secondDatasource,所以需要在cn.chriswood.anthill.example.persistence.other包下添加如下配置文件
#@Configuration
#@EnableTransactionManagement
#@EnableJpaRepositories(
#entityManagerFactoryRef = "secondEntityManagerFactory",
#transactionManagerRef = "secondTransactionManager",
#)
#class SecondJpaConfigurations
~~~

