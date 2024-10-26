import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.github.goooler.shadow") version "8.1.7"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "dev.piotrulla.hypemc.signgui"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.stellardrift.ca/repository/snapshots/")
    maven("https://repo.eternalcode.pl/snapshots")
    maven("https://repo.eternalcode.pl/releases")
}

dependencies {
    implementation(project(":newbieprotection-api"))

    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    // adventure messaging
    implementation("net.kyori:adventure-platform-bukkit:4.3.3")
    implementation("net.kyori:adventure-text-minimessage:4.18.0-SNAPSHOT")

    // config system
    val okaeriConfigsVersion = "5.0.5"
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:${okaeriConfigsVersion}")

    // commands framework
    val liteCommandsVersion = "3.6.1"
    implementation("dev.rollczi:litecommands-bukkit:$liteCommandsVersion")
    implementation("dev.rollczi:litecommands-adventure:$liteCommandsVersion")

    // eternalcode stuff
    implementation("com.eternalcode:eternalcode-commons-adventure:1.1.4-SNAPSHOT")
    implementation("com.eternalcode:multification-okaeri:1.1.4-SNAPSHOT")
    implementation("com.eternalcode:multification-bukkit:1.1.4-SNAPSHOT")

    // bStats system
    implementation("org.bstats:bstats-bukkit:3.0.0")

    // placeholders
    compileOnly("me.clip:placeholderapi:2.11.6")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

bukkit {
    main = "dev.piotrulla.newbieprotection.NewbieProtectionPlugin"
    apiVersion = "1.13"
    prefix = "ptrlNewbieProtection"
    author = "Piotrulla"
    name = "ptrlNewbieProtection"
    version = "${project.version}"
}

tasks {
    runServer {
        minecraftVersion("1.18.2")
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("ptrlNewbieProtection -${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**",
    )

    val prefix = "dev.piotrulla.newbieprotection.shared"

    listOf(
        "dev.rollczi",
        "eu.okaeri",
        "com.eternalcode",
        "net.kyori",
        "org.bstats",
        "org.yaml"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}