import org.springframework.boot.gradle.tasks.run.BootRun

tasks.withType<BootRun> {
    println(">>>>>>>>>>>>>>>>>>>>>>>> BootRun configuration")
    doFirst {
        println(">>>>>>>>>>>>>>>>>>>>>>>> BootRun doFirst")
    }
    doLast {
        println(">>>>>>>>>>>>>>>>>>>>>>>> BootRun doLast")
    }

}
task("my_task") {
    println(">>>>>>>>>>>>>>>>>>>>>>>> my_task configuration")
    doFirst {
        println(">>>>>>>>>>>>>>>>>>>>>>>> my_task doFirst")
        actions.forEach {
            if (it is Task)
                println(it.name)
        }
    }
    doLast {
        println(">>>>>>>>>>>>>>>>>>>>>>>> my_task doLast")
    }

}

dependencies {
    kapt("com.mybatis-flex:mybatis-flex-processor:1.7.9")

    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
//    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-jpa")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-mybatisflex")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-redis")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-auth")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-annotation")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-web-aliyun-oss-sts")))
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
