import org.gradle.api.JavaVersion.VERSION_1_8


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
