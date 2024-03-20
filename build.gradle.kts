plugins {
    kotlin("jvm") version "1.9.21"
    id("fr.il_totore.manadrop") version "0.4.3"
}

group = "maliwan.mcbl"
version = "0.1"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
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