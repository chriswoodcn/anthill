val appName: String by extra.properties
rootProject.name = appName

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
include("anthill-example-jpa-multi")
include("anthill-example-jpa-dynamic")
include("anthill-example-flex")
//include("anthill-reactive-example")
