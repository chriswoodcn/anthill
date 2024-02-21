# anthill-infrastructure-web

提供框架web基础能力支持

## Module Dependencies

~~~
1.anthill-infrastructure-core
2.anthill-infrastructure-json
~~~

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

