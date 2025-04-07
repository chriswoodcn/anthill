dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    api("com.xuxueli:xxl-job-core:2.3.1")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
}
