# anthill-infrastructure-redis

提供框架redis支持，底层依赖redisson

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
```

## Configurations

~~~yml
anthill:
  redisson:
    enabled: true  #默认开启redisson
    # 配置项目redis存储前缀
    keyPrefix: ${anthill.application.name}
    # 单节点配置
    singleServerConfig:
      # 客户端名称
      clientName: ${anthill.application.name}
      # 最小空闲连接数
      connectionMinimumIdleSize: 8
      # 连接池大小
      connectionPoolSize: 32
      # 连接空闲超时，单位：毫秒
      idleConnectionTimeout: 10000
      # 命令等待超时，单位：毫秒
      timeout: 3000
      # 发布和订阅连接池大小
      subscriptionConnectionPoolSize: 50
      # 也可以进行集群配置

spring:
  data:
    redis:
      # 地址
      host: localhost
      # 端口，默认为6379
      port: 6379
      # 数据库索引
      database: 0
      # 密码(如没有密码请注释掉)
      # password:
      # 连接超时时间
      timeout: 10s
      # 是否开启ssl
      ssl.enabled: false
~~~

