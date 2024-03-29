dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    // mybatis-flex官方文档   https://mybatis-flex.com/zh/intro/what-is-mybatisflex.html
    api("com.mybatis-flex:mybatis-flex-spring-boot3-starter:1.7.9")
    api("com.zaxxer:HikariCP")
    compileOnly("org.springframework:spring-web")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
