import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.github.goooler.shadow") version "8.1.7"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "dev.piotrulla.newbieprotection"
version = "1.0.2"

repositories {
    mavenCentral()

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.stellardrift.ca/repository/snapshots/")
    maven("https://repo.codemc.io/repository/maven-releases/")
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
    val eternalcodeVersion = "1.1.4-SNAPSHOT"
    implementation("com.eternalcode:eternalcode-commons-adventure:$eternalcodeVersion")
    implementation("com.eternalcode:multification-okaeri:$eternalcodeVersion")
    implementation("com.eternalcode:multification-bukkit:$eternalcodeVersion")

    // packetevents
    val scoreboardLibraryVersion = "2.2.1"
    implementation("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    runtimeOnly("net.megavex:scoreboard-library-implementation:$scoreboardLibraryVersion")
    runtimeOnly("net.megavex:scoreboard-library-packetevents:$scoreboardLibraryVersion")
    compileOnly("com.github.retrooper:packetevents-spigot:2.5.0")

    // bStats system
    implementation("org.bstats:bstats-bukkit:3.0.2")

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
    website = "https://github.com/P1otrulla/ptrlNewbieProtection"
    description = "Secure your newbie!"
    prefix = "ptrlNewbieProtection"
    version = "${project.version}"
    name = "ptrlNewbieProtection"
    author = "Piotrulla"
    apiVersion = "1.13"
    softDepend = listOf("PacketEvents")
}

tasks {
    runServer {
        minecraftVersion("1.18.2")
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("ptrlNewbieProtection - ${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**",
    )

    val prefix = "dev.piotrulla.newbieprotection.shared"

    listOf(
        "com.eternalcode",
        "dev.rollczi",
        "eu.okaeri",
        "org.bstats",
        "org.yaml",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}