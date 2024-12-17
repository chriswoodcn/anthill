# anthill-infrastructure-json

提供框架jackson配置，提供json解析工具

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
com.fasterxml.jackson.core:jackson-databind(版本根据当前springboot版本的dependency_version)
com.fasterxml.jackson.datatype:jackson-datatype-jsr310(版本根据当前springboot版本的dependency_version)
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
```

## Configurations

~~~yml
anthill:
    jackson:
        enabled: true #默认开启自定义Jackson配置
~~~

