
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    kotlin("kapt") version "1.9.22"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val jvmTargetValue: String by project

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTargetValue
    }
}

task("my_task") {
    println(">>>>>>>>>>>>>>>>>>>>>>>> my_task start")
    val taskList = tasks.toMutableList()
    taskList.forEach {
        if (it.group == "build")
            println(it.name)
    }
}

dependencies {
    kapt("com.mybatis-flex:mybatis-flex-processor:1.7.9")

//    implementation(libs.bundles.anthill)
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
//    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-jpa")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-mybatisflex")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-annotation")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-spring-doc")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-mongo")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-mail")))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.yaml:snakeyaml")
    implementation("org.slf4j:slf4j-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}
