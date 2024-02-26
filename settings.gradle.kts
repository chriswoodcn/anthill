pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring-plug")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/grails-core")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/apache-snapshots")
        }
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            credentials {
                username = "622562d6944dcd36d9e2186e"
                password = "btIFsIsDHEbX"
            }
            url = uri("https://packages.aliyun.com/maven/repository/2138380-release-8bpQtr/")
        }
        maven {
            credentials {
                username = "622562d6944dcd36d9e2186e"
                password = "btIFsIsDHEbX"
            }
            url = uri("https://packages.aliyun.com/maven/repository/2138380-snapshot-3ojMOB/")
        }
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/groups/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        }
        maven {
            url = uri("https://repo.spring.io/libs-spring-framework-build")
        }
        maven {
            url = uri("https://repo.spring.io/plugins-release")
        }
        mavenCentral()
    }
}

rootProject.name = "anthill"

println("gradle构建生命周期>>>>>>>>>>     文件=settings.gradle     阶段=initialization phase")
rootDir.listFiles()?.filter {
    it.name.startsWith("anthill-infrastructure") && it.isDirectory
}?.forEach {
    it.listFiles()?.filter { pit ->
        pit.isDirectory && (File(pit, "build.gradle.kts").exists())
    }?.forEach { pit ->
        println(pit.name)
        include("anthill-infrastructure:${pit.name}")
        findProject(":anthill-infrastructure:${pit.name}")?.name = pit.name
    }
}
include("anthill-example")
