dependencies {
    api(libs.hutool.all)
    api(libs.nashorn.js)
    api(libs.google.libphonenumber)
    api(libs.easyexcel)
    implementation("org.apache.commons:commons-lang3")
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.boot:spring-boot-starter-validation")
    compileOnly(kotlin("reflect"))
}
