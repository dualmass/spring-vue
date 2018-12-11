import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = """ `Backend` module """

plugins {
    base
    java
    application
    id("org.springframework.boot") version "2.1.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.github.johnrengelman.shadow") version "4.0.3"
}
apply { plugin("com.github.johnrengelman.shadow") }

application {
    mainClassName = "ru.steklopod.BackendApplication"
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

val junitJupiterEngineVersion = "5.3.0"
val springBootVersion = "2.1.0.RELEASE"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.projectlombok:lombok:1.18.4")

    runtime("org.springframework.boot:spring-boot-devtools:$springBootVersion")
    runtime("com.h2database:h2:1.4.197")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterEngineVersion")

}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

apply(from = "serverTasks.gradle.kts")

val frontenFolder = "../frontend"

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
    
}