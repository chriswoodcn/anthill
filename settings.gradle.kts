pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        mavenCentral()
    }
}

rootProject.name = "anthill"

println("gradle构建生命周期>>>>>>>>>>     文件=settings.gradle     阶段=initialization phase")
rootDir.listFiles()?.filter {
    it.isDirectory &&
            (File(it, "build.gradle.kts").exists()
                    || File(it, "build.gradle").exists())
}?.forEach {
    include(it.name)
}

