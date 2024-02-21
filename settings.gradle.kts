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

//println("gradle构建生命周期>>>>>>>>>>     文件=settings.gradle     阶段=initialization phase")
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
include("anthill-infrastructure:anthill-infrastructure-sms")
findProject(":anthill-infrastructure:anthill-infrastructure-sms")?.name = "anthill-infrastructure-sms"
include("anthill-infrastructure:anthill-infrastructure-annotation")
findProject(":anthill-infrastructure:anthill-infrastructure-annotation")?.name = "anthill-infrastructure-annotation"
