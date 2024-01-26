import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

val jvmTargetValue: String by project
val hutoolVersion: String by project
val lang3Version: String by project

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTargetValue
    }
}

dependencies {
    implementation("org.springframework:spring-context-support")
    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    runtimeOnly("org.springframework.boot:spring-boot-properties-migrator")
    implementation("org.apache.commons:commons-lang3:${lang3Version}")
    api("cn.hutool:hutool-all:${hutoolVersion}")
    testImplementation(kotlin("test"))
}

