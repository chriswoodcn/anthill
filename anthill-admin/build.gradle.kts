allprojects {
    ext {
        set("publishReleasesRepoUrl", "https://packages.aliyun.com/maven/repository/2138380-release-8bpQtr/")
        set("publishSnapshotsRepoUrl", "https://packages.aliyun.com/maven/repository/2138380-snapshot-3ojMOB/")
        set("publishUser", findProperty("ali.user") as String)
        set("publishPass", findProperty("ali.pass") as String)
    }
    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"; url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            name = "aliyun-central"; url = uri("https://maven.aliyun.com/repository/central")
        }
//        maven {
//            credentials {
//                username = project.ext["publishUser"] as String
//                password = project.ext["publishPass"] as String
//            }
//            url = uri(project.ext["publishReleasesRepoUrl"] as String)
//        }
//        maven {
//            credentials {
//                username = project.ext["publishUser"] as String
//                password = project.ext["publishPass"] as String
//            }
//            url = uri(project.ext["publishSnapshotsRepoUrl"] as String)
//        }
        mavenCentral()
    }
    buildscript {
        repositories {
            maven { name = "aliyun-gradle-plugin"; url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
            maven { name = "M2"; url = uri("https://plugins.gradle.org/m2/") }
        }
    }
    group = findProperty("app.group") as String
    version = findProperty("app.version") as String
}

//https://docs.spring.io/spring-framework/reference/

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("plugin.allopen") version "1.9.24" //解决final问题
    kotlin("plugin.noarg") version "1.9.24" //解决空构造函数问题
}
noArg {
    annotation("cn.chriswood.anthill.infrastructure.core.annotation.NoArgs")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
allOpen {
    annotation("cn.chriswood.anthill.infrastructure.core.annotation.AllOpen")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
kapt {
    javacOptions {
        option("querydsl.entityAccessors", true)
    }
    arguments {
        arg("plugin", "com.querydsl.apt.jpa.JPAAnnotationProcessor")
    }
}

dependencies {
    implementation(libs.bundles.anthill) { isChanging = true }
    runtimeOnly(libs.mysql.driver)
    implementation(variantOf(libs.querydsl.jpa) { classifier("jakarta") })
    kapt(variantOf(libs.querydsl.apt) { classifier("jakarta") })
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.yaml:snakeyaml")
    implementation("org.slf4j:slf4j-api")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.create("bootRunMainClassName") {
    doLast {
        springBoot.mainClass.get()
    }
}
