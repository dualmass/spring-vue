import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.internal.impldep.org.junit.platform.launcher.EngineFilter.includeEngines

description = """ `Backend` module """
//group = "br.com.befullstack.springvue"

plugins {
    base
    java
    application
    id("org.springframework.boot") version "2.1.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

application {
    mainClassName = "br.com.befullstack.springvue.backend.BackendApplication"
}

buildscript {
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
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
//    implementation( "org.springframework.boot:spring-boot-starter-data-mongodb:$springBootVersion"
    implementation("org.projectlombok:lombok:1.16.22")
    runtime("org.springframework.boot:spring-boot-devtools:$springBootVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterEngineVersion")
}

configure<JavaPluginConvention> {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

val frontenFolder = "../frontend"

tasks {
    val buildFrontend by registering(Exec::class) {
        npmExecute("npmBuild")
    }

    val runFrontend by registering(Exec::class) {
        npmExecute("npmRunServe")
    }

    val jar by existing {
        dependsOn(buildFrontend)
        copy {
            from(file("$frontenFolder/dist"))
            into("public")
        }
    }

    val bootJar by existing {
        finalizedBy(runFrontend)
    }

    withType<ProcessResources> {
        //TODO
    }

    getByName<Test>("test") {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }
    }
}


fun Exec.npmExecute(vararg commands: String) {
    val commandsList = listOf("cmd", "/c", "gradle", *commands)
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    with(this) {
        workingDir(frontenFolder)
        if (isWindows) commandLine(commandsList)
        else commandLine(commandsList.drop(2))
    }
}

