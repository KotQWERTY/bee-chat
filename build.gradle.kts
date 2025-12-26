plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.github.kotqwerty"
version = "1.7.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("com.charleskorn.kaml:kaml:0.70.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        relocate("org.bstats", "$group.beechat.metrics")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

kotlin {
    jvmToolchain(21)
}
