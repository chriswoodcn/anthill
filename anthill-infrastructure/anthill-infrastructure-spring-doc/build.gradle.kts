dependencies {
    api(libs.bundles.spring.doc)
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
