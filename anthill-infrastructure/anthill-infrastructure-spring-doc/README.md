# anthill-infrastructure-doc

提供框架springdoc支持

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
```

## Configurations

~~~yml
anthill:
    swagger:
        enabled: true #默认开启springdoc配置和swagger配置
        info:
            title: xxxx
            description: xxxx
            version: xxxx
        contact:
            name: xxx
            email: xxx@aliyun.com
        #...
~~~

