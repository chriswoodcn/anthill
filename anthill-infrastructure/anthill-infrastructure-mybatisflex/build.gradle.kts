dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    // mybatis-flex官方文档   https://mybatis-flex.com/zh/intro/what-is-mybatisflex.html
    api("com.mybatis-flex:mybatis-flex-spring-boot3-starter:1.9.9")
    //kotlin扩展库
    api("com.mybatis-flex:mybatis-flex-kotlin-extensions:1.1.2")
    api("com.zaxxer:HikariCP")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
