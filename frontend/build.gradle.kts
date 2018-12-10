import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.task.NodeTask
import com.sun.deploy.config.JREInfo.setArgs

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

    val npmRunServe by registering(NpmTask::class) {
        group = "Npm"
        setArgs(listOf("run", "serve"))
        project.logger.info(">>> `npm INSTALL` is done ")
    }

    val npmBuild by registering(NpmTask::class) {
        group = "Npm"
        setArgs(listOf("run", "build"))
        doLast { project.logger.info(">>> `npm run BUILD` is done.") }
    }

    val npmInstall by existing(NpmTask::class) {
        group = "Npm"
        finalizedBy(npmBuild)
        project.logger.info(">>> `npm INSTALL` is done ")
    }

    val copyToBackendAndRun by registering(Copy::class) {
        group = "Npm"
        from(file("dist"))
        into("$backendFolder/public")
        finalizedBy(npmRunServe)
        project.logger.info(">>> Frontend dist folder succesfully copied. ")
    }

    val clean by registering(Delete::class) {
        group = "Npm"
        delete("node"); delete("dist")
        delete("node_modules"); delete("$backendFolder/src/main/public")
    }
}

node { download = false } //TODO (true - if need)
