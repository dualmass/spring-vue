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

    val run by creating(Exec::class) {
        group = "Npm"
        npmRun("serve")
        doLast { println(">>> `npm INSTALL` is done ") }
    }

    val npmBuild by creating(Exec::class) {
        group = "Npm"
        npmRun("build")
        doLast { println(">>> `npm run BUILD` is done.") }
    }

    withType<NpmTask> { finalizedBy(npmBuild) }

    create("magic") {
        group = "Npm"
        dependsOn(npmInstall)
        finalizedBy(run)
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


fun Exec.npmRun(vararg commands: String) {
    val commandsList = listOf("cmd", "/c", "npm", "run", *commands)
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    with(this) {
        if (isWindows) commandLine(commandsList)
        else commandLine(commandsList.drop(2))
    }
}