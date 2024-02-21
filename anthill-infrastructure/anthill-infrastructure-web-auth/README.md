# anthill-infrastructure-web-auth

提供框架web鉴权支持，基于satoken jwt redis

## Module Dependencies

~~~
1.anthill-infrastructure-core
2.anthill-infrastructure-json
3.anthill-infrastructure-redis
4.anthill-infrastructure-web
~~~

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

