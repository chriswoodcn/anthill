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
val artifactId = "anthill-infrastructure-mail"

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
        val releasesRepoUrl = uri(rootProject.ext["publishReleasesRepoUrl"] as String)
        val snapshotsRepoUrl = uri(rootProject.ext["publishSnapshotsRepoUrl"] as String)
        maven {
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = rootProject.ext["publishUser"] as String
                password = rootProject.ext["publishPass"] as String
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
    api(libs.bundles.spring.mail)
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("org.slf4j:slf4j-api")
    testImplementation(kotlin("test"))
}
