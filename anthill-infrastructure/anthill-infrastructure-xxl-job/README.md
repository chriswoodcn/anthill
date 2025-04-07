# anthill-infrastructure-xxl-job

提供框架xxl-job支持

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
```

## Configurations

~~~yml
anthill:
  xxl-job:
    enabled: true  #是否开启websocket 默认不开启
    path: /websocket  #默认路径/websocket 
    allowedOrigins: #设置访问源地址 默认都放行
~~~
