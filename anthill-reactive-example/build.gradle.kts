dependencies {

    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-core")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-json")))
    implementation(project(mapOf("path" to ":anthill-infrastructure:anthill-infrastructure-reactive-web")))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    //webflux kt扩展
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    //异步数据库支持
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    //mysql 的异步数据库支持
    implementation("io.asyncer:r2dbc-mysql:1.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.yaml:snakeyaml")
    implementation("org.slf4j:slf4j-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    //webflux 返回值的协程支持
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    //webflux await的协程支持
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}
