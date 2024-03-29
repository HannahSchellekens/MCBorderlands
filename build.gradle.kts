import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

plugins {
    kotlin("jvm") version "1.9.21"
    id("fr.il_totore.manadrop") version "0.4.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "maliwan.mcbl"
version = "0.1"

val jarFileName: String
    get() = "${rootProject.name}-${version}-all.jar"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    testCompileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

spigot {
    desc {
        named("MCBorderlands")
        version("${project.version}")
        authors("Hannah Schellekens")
        main("maliwan.mcbl.MCBorderlandsPluginKt")
        apiVersion("1.20")
        prefixed("MCBL")
    }
}

tasks.register("jarDeploy") {
    dependsOn(tasks.shadowJar)
    description = "Builds jar and deploys to server."
    doLast {
        val destination = File(System.getenv("PLUGIN_FOLDER_PATH") + "/$jarFileName")
        val outputFile = File("${project.rootDir}/build/libs/$jarFileName")
        Files.copy(outputFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("Copied $outputFile to ${destination.absoluteFile}")
    }
}