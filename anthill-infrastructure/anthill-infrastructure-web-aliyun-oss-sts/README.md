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
  aliyun-oss-sts:
    enabled: true
    oss:
      primary:
        region-id: cn-shanghai
        access-key-id: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        access-key-secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        role-arn: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        role-session-name: anthill-example
      cn-shanghai:
        region-id: cn-shanghai
        access-key-id: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        access-key-secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        role-arn: xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        role-session-name: anthill-example
~~~

