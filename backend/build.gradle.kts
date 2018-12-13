import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = """ `Backend` module """

plugins {
    val kotlinVersion = "1.3.11"
    java
    application
    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
}
apply { plugin("com.github.johnrengelman.shadow") }

application {
    mainClassName = "ru.steklopod.Application.kt"
}

buildscript {
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:4.0.3")
        classpath("org.springframework.boot:spring-boot-gradle-plugin")
    }
}

repositories {
    jcenter()
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
}

val springBootVersion = "2.1.1.RELEASE"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") { exclude(module = "spring-boot-starter-tomcat") }
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.projectlombok:lombok")

    runtime("org.springframework.boot:spring-boot-devtools")
    runtime("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test") { exclude(module = "junit") }
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Jackson Dependencies
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    runtime("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    runtime("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}


tasks {
    withType<ShadowJar> {
        html@ //imperceptiblethoughts.com/shadow/getting-started/#default-java-groovy-tasks
        enabled = false
        baseName = "admin-cache"
        classifier = ""
        version = ""
        minimize()
//        distributions( from("public") )
        html@ //github.com/johnrengelman/shadow/tree/master/src/docs/configuration/merging
        mergeServiceFiles()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

apply(from = "serverTasks.gradle.kts")
