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
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    implementation("org.slf4j:slf4j-api")
    api("cn.hutool:hutool-all:${hutoolVersion}")
    testImplementation(kotlin("test"))
}

//println("gradle构建生命周期>>>>>>>>>>     文件=subject.build.gradle     阶段=configuration phase")
//
//tasks.register("configured") {
//    println("gradle构建生命周期>>>>>>>>>>     task=configured     阶段=configuration phase")
//}
//
//tasks.register("test1") {
//    doLast {
//        println("gradle构建生命周期>>>>>>>>>>     task=test1     阶段=execution phase.")
//    }
//    println("gradle构建生命周期>>>>>>>>>>     task=test1     阶段=configuration phase")
//}
//
//tasks.register("testBoth1") {
//    doFirst {
//        println("gradle构建生命周期>>>>>>>>>>     doFirst task=testBoth1     阶段=execution phase")
//    }
//    doLast {
//        println("gradle构建生命周期>>>>>>>>>>     doLast task=testBoth1     阶段=execution phase")
//    }
//    println("gradle构建生命周期>>>>>>>>>>     task=testBoth1     阶段=configuration phase")
//}
//
//tasks.register("aaa") {
//    println("task create aaa")
//}
//
//tasks.named("aaa") {
//    println("task named aaa")
//}
//
//repeat(4) { counter ->
//    tasks.register("task$counter") {
//        doLast {
//            println("I'm task number $counter")
//        }
//    }
//}
//
//tasks.register("taskX") {
//    dependsOn("taskY")
//    doLast {
//        println("taskX")
//    }
//}
//
//tasks.register("taskY") {
//    doLast {
//        println("taskY")
//    }
//}
//
//tasks.register("hello") {
//    group = "Custom"
//    description = "A lovely greeting task."
//    doLast {
//        println("Hello world!")
//    }
//    dependsOn(tasks.build)
//}

