plugins {
    `kotlin-dsl`
    id("com.gradleup.shadow") version "9.0.0-beta13"
}

group = "me.raider"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.12")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.12")
}

tasks.shadowJar {
    archiveBaseName.set("BlockPlacer")
    archiveClassifier.set("") // The output JAR will not have the -all suffix
    relocate("io.github.revxrsal", "me.raider.lamp")
}

tasks.build {
    dependsOn(tasks.shadowJar) // Ensure shadowJar runs on build
}