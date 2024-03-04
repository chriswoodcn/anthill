dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    compileOnly("org.springframework.boot:spring-boot-starter-websocket")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
}
