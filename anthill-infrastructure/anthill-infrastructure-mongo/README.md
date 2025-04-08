# anthill-infrastructure-mongo

提供框架mongodb支持

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-starter-data-mongodb:$springboot_version
org.springframework.boot:spring-boot-starter-aop:$springboot_version
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
org.jetbrains.kotlin:kotlin-reflect:$kotlin_version
```

## Configurations

~~~yml
---
anthill:
  mongo:
    enabled: true #开启mongodb
    type: single #单体数据源
    transaction:
      enabled: true #开启事务管理 只有mongodb副本集或集群才可以开启
    dbs:
      primary: #主数据源必须primary
        uri: mongodb://admin:xxx@192.168.2.188:27017/xxx?authSource=admin&directConnection=true

#应用主入口排除mongodb的自动配置
#@SpringBootApplication(exclude = [MongoDataAutoConfiguration::class,MongoAutoConfiguration::class])

#配置EnableMongoRepositories
#@Configuration
#@EnableMongoRepositories(
#basePackages = ["xxx.xxx.xxx.repository"],
#repositoryBaseClass = SingleMongoRepository::class,
#mongoTemplateRef = "singleMongoTemplate"
#)
#class MongoDbRepositoryConfiguration

---
anthill:
  mongo:
    enabled: true #开启mongodb
    type: single #单体数据源
    transaction:
      enabled: true #开启事务管理 只有mongodb副本集或集群才可以开启
    dbs:
      primary: #主数据源必须primary
        uri: mongodb://admin:xxx@192.168.2.188:27017/xxx?authSource=admin&directConnection=true
      secondary:
        uri: mongodb://admin:xxx@192.168.2.188:27017/xxx?authSource=admin&directConnection=true
      other:
          uri: mongodb://admin:xxx@192.168.2.188:27017/xxx?authSource=admin&directConnection=true

#应用主入口排除mongodb的自动配置
#@SpringBootApplication(exclude = [MongoDataAutoConfiguration::class,MongoAutoConfiguration::class])

#配置EnableMongoRepositories
#@Configuration
#@EnableMongoRepositories(
#basePackages = ["xxx.xxx.xxx.repository"],
#repositoryBaseClass = DynamicMongoRepository::class,
#mongoTemplateRef = "dynamicMongoTemplate"
#)
#class MongoDbRepositoryConfiguration

#使用注解切换数据源
#@DynamicMongoDB("primary")
#@DynamicMongoDB("secondary")
#@DynamicMongoDB("other")
~~~
