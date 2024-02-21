import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

val jvmTargetValue: String by project

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTargetValue
    }
}
dependencies {
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
