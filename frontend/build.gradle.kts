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

    val npmRun by registering(Exec::class) {
        group = "Npm"
        npmExecute("run", "serve")
        doLast { println(">>> `npm INSTALL` is done ") }
    }

    val npmBuild by registering(Exec::class) {
        group = "Npm"
        npmExecute("run", "build")
        doLast { println(">>> `npm run BUILD` is done.") }
    }

    val npmInstall by existing {
        finalizedBy(npmBuild)     //TODO ( хук не привязан )
        doLast { println(">>> `npm INSTALL` is done ") }
    }

    create("magic") {
        group = "Npm"
        dependsOn(npmInstall)
        finalizedBy(npmRun)
        doLast { println(">>> `npm INSTALL + BUIlD + RUN` is done ") }
    }

    val clean by registering(Delete::class) {
        group = "Npm"
        delete("dist"); delete("target/dist"); delete("$backendFolder/src/main/public"); delete("node_modules");
        doLast { println(">>> Ok. Cleared <<<") }
    }

    val copyToBackendPublic by registering(Copy::class) {
        group = "Dist"
        from(file("dist"))
        into("$backendFolder/public")
        doLast { println(">>> Frontend dist folder succesfully copied. ") }
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