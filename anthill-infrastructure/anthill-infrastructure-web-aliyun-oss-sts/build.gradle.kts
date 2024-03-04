dependencies {
    api("com.aliyun:aliyun-java-sdk-core:4.6.1")
    api("com.aliyun:aliyun-java-sdk-sts:3.1.2")
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
}
