import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    val aliUsername: String by extra.properties
    val aliPassword: String by extra.properties
    val aliReleasesUrl: String by extra.properties
    val aliSnapshotUrl: String by extra.properties
    val appGroup : String by extra.properties
    val appVersion : String by extra.properties
    repositories {
        mavenLocal()
        maven {
            credentials {
                username = aliUsername
                password = aliPassword
            }
            url = uri(aliReleasesUrl)
        }
        maven {
            credentials {
                username = aliUsername
                password = aliPassword
            }
            url = uri(aliSnapshotUrl)
        }
    }
    buildscript {
        repositories {
            mavenLocal()
            maven {
                credentials {
                    username = aliUsername
                    password = aliPassword
                }
                url = uri(aliReleasesUrl)
            }
            maven {
                credentials {
                    username = aliUsername
                    password = aliPassword
                }
                url = uri(aliSnapshotUrl)
            }
        }
    }
    group = appGroup
    version = appVersion
}

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)  //解决final问题
    alias(libs.plugins.kotlin.noarg) //解决空构造函数问题
    alias(libs.plugins.kotlin.kapt)
    `maven-publish`
}

val pLibs = libs
subprojects {

    val aliUsername: String by extra.properties
    val aliPassword: String by extra.properties
    val aliReleasesUrl: String by extra.properties
    val aliSnapshotUrl: String by extra.properties
    val appGroup : String by extra.properties
    val appVersion : String by extra.properties

    apply {
        plugin(pLibs.plugins.spring.boot.get().pluginId)
        plugin(pLibs.plugins.spring.dependency.get().pluginId)
        plugin(pLibs.plugins.kotlin.jvm.get().pluginId)
        plugin(pLibs.plugins.kotlin.spring.get().pluginId)
        plugin(pLibs.plugins.kotlin.allopen.get().pluginId)
        plugin(pLibs.plugins.kotlin.noarg.get().pluginId)
    }
    noArg {
        annotation("cn.chriswood.anthill.infrastructure.core.annotation.NoArgs")
    }
    allOpen {
        annotation("cn.chriswood.anthill.infrastructure.core.annotation.AllOpen")
    }

    java.sourceCompatibility = JavaVersion.VERSION_17

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "17"
        }
    }

    if (project.projectDir.name.startsWith("anthill-example-")) {
        apply {
            plugin(pLibs.plugins.kotlin.kapt.get().pluginId)
        }

    }
    if (project.projectDir.name.startsWith("anthill-infrastructure-")) {
        apply {
            plugin(pLibs.plugins.kotlin.kapt.get().pluginId)
            plugin("maven-publish")
        }

        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = "${project.group}"
                    artifactId = project.projectDir.name
                    version = "${project.version}"

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
//                maven {
//                    url = if (version.toString().endsWith("SNAPSHOT")) uri(aliSnapshotUrl) else uri(aliReleasesUrl)
//                    credentials {
//                        username = aliUsername
//                        password = aliPassword
//                    }
//                }
                mavenLocal()
            }
        }

        dependencies {
            annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
            kapt("org.springframework.boot:spring-boot-configuration-processor")
            compileOnly("org.slf4j:slf4j-api")
            testImplementation(kotlin("test"))
        }
    }
}

/**
 * implementation 依赖不可传递 会将对应的依赖添加到编译路径，并将依赖打包输出
 *
 * Gradle会将依赖项添加到编译类路径，并将依赖项打包到 build 输出。
 * 不过，当您的模块配置 implementation 依赖项时，会让Gradle了解您不希望该模块在编译时将该依赖项泄露给其他模块。
 * 也就是说，其他模块只有在运行时才能使用该依赖项。使用此依赖项配置代替 api 或 compile（已废弃）可以显著缩短
 * 构建时间，因为这样可以减少构建系统需要重新编译的模块数。例如，如果 implementation 依赖项更改了其 API，
 * Gradle 只会重新编译该依赖项以及直接依赖于它的模块。大多数应用和测试模块都应使用此配置。
 */
/**
 * api 依赖可传递 会将对应的依赖添加到编译路径，并将依赖打包输出
 *
 * Gradle会将依赖项添加到编译类路径和 build 输出。当一个模块包含 api 依赖项时，会让 Gradle
 * 了解该模块要以传递方式将该依赖项导出到其他模块，以便这些模块在运行时和编译时都可以使用该依赖项。
 * 此配置的行为类似于 compile （ 现已废弃 ），但使用它时应格外小心，只能对您需要以传递方式导出到
 * 其他上游消费者的依赖项使用它。 这是因为，如果 api 依赖项更改了其外部 API ， Gradle 会在编译时
 * 重新编译所有有权访问该依赖项的模块。 因此，拥有大量的 api 依赖项会显著增加构建时间。 除非要
 * 将依赖项的 API 公开给单独的模块，否则库模块应改用 implementation 依赖项。
 */
/**
 * compile
 * Gradle会将依赖项添加到编译类路径和 build 输出，并将依赖项导出到其他模块。
 * 此配置已废弃（在 AGP 1.0 - 4.2 中可用 ）。
 */
/**
 * compileOnly 依赖不可传递 会添加到编译路径中，但是不会打包，只能在编译时访问
 *
 * Gradle只会将依赖项添加到编译类路径（也就是说，不会将其添加到 build 输出）。如果创建Android
 * 模块时在编译期间需要相应依赖项，但它在运行时可有可无，此配置会很有用。如果您使用此配置，那么您
 * 的库模块必须包含一个运行时条件，用于检查是否提供了相应依赖项，然后适当地改变该模块的行为，以使
 * 该模块在未提供相应依赖项的情况下仍可正常运行。这样做不会添加不重要的瞬时依赖项，因而有助于减小
 * 最终应用的大小。 此配置的行为类似于 provided （ 现已废弃 ）。
 */
/**
 * provided Gradle 只会将依赖项添加到编译类路径（也就是说，不会将其添加到 build 输出）。
 * 此配置已废弃（在 AGP 1.0 - 4.2 中可用 ）。
 */
/**
 * annotationProcessor 用于注解处理器的依赖配置
 *
 * 如需添加对作为注解处理器的库的依赖，必须使用 annotationProcessor配置将其添加到注解处理器的类路径。
 * 这是因为，使用此配置可以将编译类路径与注解处理器类路径分开，从而提高build性能 。 如果 Gradle在编译
 * 类路径上找到注解处理器，则会禁用避免编译功能，这样会对构建时间产生负面影响（ Gradle 5.0 及更高版本
 * 会忽略在编译类路径上找到的注解处理器 ）。如果 JAR 文件包含以下文件，则 Android Gradle插件会假定
 * 依赖项是注解处理器 ： META -INF / services / javax.annotation.processing.Processor如果
 * 插件检测到编译类路径上包含注解处理器，则会产生 build 错误。Kotlin使用kapt / ksp。
 */
/**
 * testXXXX testImplementation androidTestImplementation
 * 用于指定在测试代码的依赖
 */
