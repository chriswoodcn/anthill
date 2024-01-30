import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

val jvmTargetValue: String by project

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTargetValue
    }
}
dependencies {
    implementation(
        project(
            mapOf(
                "path" to ":anthill-infrastructure:anthill-infrastructure-json",
                "path" to ":anthill-infrastructure:anthill-infrastructure-spring"
            )
        )
    )
    implementation("org.redisson:redisson:3.25.2")
    implementation("org.redisson:redisson-spring-boot-starter:3.25.2")
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
