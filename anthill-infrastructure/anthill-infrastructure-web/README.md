# anthill-infrastructure-web

提供框架web基础能力支持

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
    xss:
      enabled: false  #是否开启防止xss攻击过滤
    invoke:
      enabled: false  #是否开启requestURI调用日志
    cors:
      enabled: true   #是否开启CORS跨域配置
    captcha:
      enabled: true   #是否开启验证码图形配置
~~~

