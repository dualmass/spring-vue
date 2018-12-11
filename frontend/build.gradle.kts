import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.task.NodeTask

description = """ `frontend` module (frontend, vue.js) """

plugins {
    //    java
    id("com.moowork.node") version "1.2.0"
}

buildscript {
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
    }
}

repositories { jcenter() }
dependencies { }


tasks {
    val backendFolder = "../backend"

    val npmRunServe by registering(Exec::class) {
        group = "Npm"
        npmExecute("run", "serve")
        println(">>> `npm INSTALL` is done ")
    }

    val npmBuild by registering(Exec::class) {
        group = "Npm"
        npmExecute("run", "build")
        println(">>> `npm run BUILD` is done.")
    }

    val npmInstall by existing(NpmTask::class) {
        finalizedBy(npmBuild); println(">>> `npm INSTALL` is done ")
    }

    val clean by registering(Delete::class) {
        group = "Npm"
        delete("node"); delete("dist"); delete("node_modules")
    }

    val copyToBackendPublic by registering(Copy::class) {
        group = "Dist"
        from(file("dist"))
        into("$backendFolder/public")
        println(">>> Frontend dist folder succesfully copied. ")
    }
}

node { download = false } //TODO ( true - если не установлен npm )


fun Exec.npmExecute(vararg commands: String) {
    val commandsList = listOf("cmd", "/c", "npm", *commands)
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    with(this) {
        if (isWindows) commandLine(commandsList)
        else commandLine(commandsList.drop(2))
    }
}