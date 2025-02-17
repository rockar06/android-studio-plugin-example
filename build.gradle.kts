import org.jetbrains.intellij.platform.gradle.Constants

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        //bundledPlugin("org.jetbrains.android")
        bundledPlugin("org.jetbrains.android")
        instrumentationTools()
        //androidStudio("2024.3.1.2")
        local(file("/Applications/Android Studio.app"))
        /*if (project.hasProperty("localIdeOverride")) {
            local(property("localIdeOverride").toString())
        } else {
            androidStudio("2024.2.2.13")
        }*/
    }
    testImplementation("junit:junit:4.13.2")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

// Workaround to exclude performancePlugin to compile
// See https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1738
// See https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1843
configurations {
    named(Constants.Configurations.INTELLIJ_PLATFORM_BUNDLED_MODULES) {
        exclude(Constants.Configurations.Dependencies.BUNDLED_MODULE_GROUP, "com.jetbrains.performancePlugin")
    }
}
