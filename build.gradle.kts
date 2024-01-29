//println("gradle构建生命周期>>>>>>>>>>     文件=root.build.gradle     阶段=configuration phase")

val groupValue: String by project
val versionValue: String by project

allprojects {
    group = groupValue
    version = versionValue
}
