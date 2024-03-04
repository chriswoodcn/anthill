dependencies {
    api(libs.bundles.spring.mail)
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
