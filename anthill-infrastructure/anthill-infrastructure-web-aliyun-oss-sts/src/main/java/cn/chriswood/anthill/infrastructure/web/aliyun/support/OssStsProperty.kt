package cn.chriswood.anthill.infrastructure.web.aliyun.support

data class OssStsProperty(
    val regionId: String,
    val accessKeyId: String,
    val accessKeySecret: String,
    val roleArn: String,
    val roleSessionName: String,
    val duration: Int = 7200
)
