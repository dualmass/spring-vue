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
    val inputVueJsCompiledFolder = "dist"
    val outBackendDir = "/build/static"


    val dist by registering(Copy::class) {
        group = "Distribut"
        description = """ Makes copy of $inputVueJsCompiledFolder folder into $outBackendDir of `server` module. """
        dependsOn("npm_run_build")
        from(file(inputVueJsCompiledFolder))
        into("static")
//        into(file("../backend/$outBackendDir"))
        doLast { println(">>> Ok. Frontend files from $inputVueJsCompiledFolder have copied.<<<") }
    }

    val npmClean by registering(Delete::class) {
        val webDir = "${rootDir}/frontend"
        delete("${webDir}/node")
        delete("$webDir/node_modules")
        delete("$webDir/dist")
        delete("$rootDir/backend/src/main/resources/public")
    }

    create<NpmTask>("npmBuild") {
        group = "Node"
        description = " Run commands: ``npm run build` & `npm run build` "
        dependsOn("npmInstall")
        setArgs(listOf("run", "build"))
        finalizedBy(dist)
        doLast { println(">>> Ok. Frontend `run build` is done.") }
    }
}

node { download = false } //TODO (true - if need)
