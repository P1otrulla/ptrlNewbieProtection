plugins {
    id("java")
    `maven-publish`
}

group = "dev.piotrulla.newbieprotection"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "$group"
            artifactId = artifactId
            version = "${project.version}"

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "piotrulla-repository"
            url = uri("https://repo.piotrulla.dev/releases")

            credentials {
                username = "admin"
                password = "5k3Ss9txOjs3MH8ElyDOLYjgxNc3SxVzxDS+FzZL5YZ0Gpw1f3DZJm2vzPm7r2Dj"
            }
        }
    }
}