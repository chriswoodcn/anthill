# anthill-infrastructure-web-auth

提供框架web鉴权支持，基于satoken jwt redis

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
org.springframework.boot:spring-boot-starter-web:$springboot_version
```

## Configurations

~~~yml
anthill:
  web:
    auth:
      enabled: true  #默认开启权限认证功能
      excludes: #权限认证排除的路径
        - /
        # 静态资源
        - /*.html
        - /**/*.html
        - /**/*.css
        - /**/*.js
        # 公共路径
        - /favicon.ico
        # springdoc 文档配置
        - /*/api-docs
        - /*/api-docs/**
        # actuator 监控配置
        - /actuator
        - /actuator/**
~~~

