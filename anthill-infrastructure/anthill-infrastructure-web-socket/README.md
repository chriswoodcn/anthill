# anthill-infrastructure-web-annotation

提供框架web注解 @RepeatLimit @RateLimit

## Module Dependencies

~~~
1.anthill-infrastructure-core
2.anthill-infrastructure-json
3.anthill-infrastructure-redis
4.anthill-infrastructure-web
5.anthill-infrastructure-web-auth
~~~

## Configurations

~~~yml
anthill:
  web:
    socket:
      enabled: true  #是否开启websocket 默认不开启
      path: /websocket  #默认路径/websocket 
      allowedOrigins: #设置访问源地址 默认都放行
~~~
