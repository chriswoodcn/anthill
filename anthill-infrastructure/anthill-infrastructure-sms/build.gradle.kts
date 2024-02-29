dependencies {
    api(libs.bundles.spring.sms4j)
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
}
