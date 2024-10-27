import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "dev.piotrulla.newbieprotection"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.piotrulla.dev/releases")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    compileOnly("dev.piotrulla.newbieprotection:newbieprotection-api:1.0.0")
}

bukkit {
    main = "dev.piotrulla.newbieprotection.Main"
    apiVersion = "1.13"
    prefix = "ptrlNewbieProtection-example"
    author = "Piotrulla"
    name = "ptrlNewbieProtection-example"
    version = "${project.version}"
    depend = listOf("ptrlNewbieProtection")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("ptrlNewbieProtection-example -${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**",
    )
}