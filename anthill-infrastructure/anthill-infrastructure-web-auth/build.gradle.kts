dependencies {
    api(libs.bundles.spring.satoken)
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
