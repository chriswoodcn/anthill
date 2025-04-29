dependencies {
    implementation("io.github.jpush:jiguang-sdk:5.1.8")
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
