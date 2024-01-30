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
    implementation("org.springframework:spring-web")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
