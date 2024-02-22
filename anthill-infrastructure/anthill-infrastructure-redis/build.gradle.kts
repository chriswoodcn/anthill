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
val artifactId = "anthill-infrastructure-redis"

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
    api(libs.bundles.spring.redisson)
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
