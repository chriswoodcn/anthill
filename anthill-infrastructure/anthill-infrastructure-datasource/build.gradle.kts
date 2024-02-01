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
    compileOnly("org.springframework:spring-web")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("com.zaxxer:HikariCP")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
