# anthill-infrastructure-sms

提供框架sms短信服务支持

## 需要依赖的包

查看当前anthill的springboot版本 $springboot_version
查看当前anthill的kotlin版本 $kotlin_version

```
org.springframework.boot:spring-boot-autoconfigure:$springboot_version
```

## Configurations

~~~yml
anthill:
  sms:
    enabled: true  #默认开启sms服务
# 参照sms4j官方文档进行配置 http://www.sms4j.com/doc3/
sms:
  # 标注从yml读取配置
  config-type: yaml
  blends:
    # 自定义的标识，也就是configId这里可以是任意值（最好不要是中文）
    tx1:
      #厂商标识，标定此配置是哪个厂商，详细请看厂商标识介绍部分
      supplier: tencent
      #您的accessKey
      access-key-id: 您的accessKey
      #您的accessKeySecret
      access-key-secret: 您的accessKeySecret
      #您的短信签名
      signature: 您的短信签名
      #模板ID 非必须配置，如果使用sendMessage的快速发送需此配置
      template-id: xxxxxxxx
      #您的sdkAppId
      sdk-app-id: 您的sdkAppId
      # 自定义的标识，也就是configId这里可以是任意值（最好不要是中文）
    tx2:
      #厂商标识，标定此配置是哪个厂商，详细请看厂商标识介绍部分
      supplier: tencent
      #您的accessKey
      access-key-id: 您的accessKey
      #您的accessKeySecret
      access-key-secret: 您的accessKeySecret
      #您的短信签名
      signature: 您的短信签名
      #模板ID 非必须配置，如果使用sendMessage的快速发送需此配置
      template-id: xxxxxxxx
      #您的sdkAppId
      sdk-app-id: 您的sdkAppId
~~~

