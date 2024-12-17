dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("com.zaxxer:HikariCP")
    compileOnly(kotlin("reflect"))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
