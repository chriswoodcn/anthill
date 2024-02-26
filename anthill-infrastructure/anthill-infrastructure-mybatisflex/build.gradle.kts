import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    `maven-publish`
}

val jvmTargetValue: String by project
val artifactId = "anthill-infrastructure-mybatisflex"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${rootProject.group}"
            artifactId
            version = "${rootProject.version}"

            pom {
                developers {
                    developer {
                        id.set("taotaozn")
                        name.set("chriswoodcn")
                        email.set("chriswoodcn@aliyun.com")
                    }
                }
            }

            from(components["java"])
        }
    }
    repositories {
        val releasesRepoUrl = uri("https://packages.aliyun.com/maven/repository/2138380-release-8bpQtr/")
        val snapshotsRepoUrl = uri("https://packages.aliyun.com/maven/repository/2138380-snapshot-3ojMOB/")
        maven {
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = "622562d6944dcd36d9e2186e"
                password = "btIFsIsDHEbX"
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = jvmTargetValue
    }
}
dependencies {
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    // mybatis-flex官方文档   https://mybatis-flex.com/zh/intro/what-is-mybatisflex.html
    api("com.mybatis-flex:mybatis-flex-spring-boot3-starter:1.7.9")
    api("com.zaxxer:HikariCP")
    compileOnly("org.springframework:spring-web")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}