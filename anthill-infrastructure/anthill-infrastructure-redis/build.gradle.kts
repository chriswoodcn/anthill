dependencies {
    api(libs.bundles.spring.redisson)
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
