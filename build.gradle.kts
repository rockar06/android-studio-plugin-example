import org.jetbrains.intellij.platform.gradle.Constants

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.1.0"
    // When updating the platform to 2.2.1, we need to specify the Android plugin instead of using "bundledPlugin" option
    // id("org.jetbrains.intellij.platform") version "2.2.1"
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
        // Android Studio Version, more https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html#2024
        //androidStudio("2024.3.1.2")

        // Local installed version from Android Studio
        local(file("/Applications/Android Studio.app"))
        /*if (project.hasProperty("localIdeOverride")) {
            local(property("localIdeOverride").toString())
        } else {
            androidStudio("2024.2.2.13")
        }*/

        // Use a bundled plugin, in this case, the "android" plugin is bundled in the Android Studio
        // Check Settings > Plugins > Installed > Search for "android" view the label "bundled"
        bundledPlugin("org.jetbrains.android")

        // Plugin version from Intellij Marketplace https://plugins.jetbrains.com/plugin/22989-android, specify only if
        // targeting a non Android Studio IDE
        //plugin("org.jetbrains.android:243.24978.46")
        //plugin("org.jetbrains.android:242.22855.74")

        // Require when targeting platform plugin below to 2.2.1
        instrumentationTools()
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
