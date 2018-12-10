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

//jar.dependsOn 'npm_run_build'

tasks {
    val runFrontEnd by registering(NpmTask::class) {
        setArgs(listOf("run", "serve"))
    }

    val dist by registering(Copy::class) {
        group = "Distribut"
        from(file("dist"))
        into("../backend/resources/public")
        doLast {
            println(">>> Ok. Frontend files from have copied.<<<")
            finalizedBy(runFrontEnd)
        }
    }
    
    create<NpmTask>("npmBuild") {
        group = "Node"
        description = " Run commands: ``npm run build` & `npm run build` "
        dependsOn("npmInstall")
        setArgs(listOf("run", "build"))
        finalizedBy(dist)
        doLast { println(">>> Ok. Frontend `run build` is done.") }
    }


    val npmClean by registering(Delete::class) {
        val webDir = "../frontend"
        delete("${webDir}/node")
        delete("$webDir/node_modules")
        delete("$webDir/dist")
        delete("../backend/src/main/resources/public")
    }
}

node { download = false } //TODO (true - if need)
