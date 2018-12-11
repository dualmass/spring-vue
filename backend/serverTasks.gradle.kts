import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.internal.impldep.org.junit.platform.launcher.EngineFilter.includeEngines

description = """ `Backend` tasks """

val frontenFolder = "../frontend"

tasks {
    val installFrontend by registering(Exec::class) {
        npmExecute("npm_install"); group = "Frontend"
    }

    val buildFrontend by registering(Exec::class) {
        npmExecute("npmBuild"); group = "Frontend"
    }

    val runFrontend by registering(Exec::class) {
        npmExecute("run"); group = "Frontend"
    }

    getByName("bootRun") {
        finalizedBy(runFrontend)
    }
    withType<ProcessResources> {
        //TODO
    }

//    //TODO ( проверить )
//    val jar by existing {
//        dependsOn(buildFrontend)
//        copy {
//            from(file("$frontenFolder/dist"))
//            into("public")
//        }
//    }


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

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
