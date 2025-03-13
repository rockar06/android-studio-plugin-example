plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.2.1"
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
        // We can use a local installation using "local" or a downloaded binary using "androidStudio"
        // Android Studio Version, more https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html#2024
        // androidStudio("2024.3.1.13")
        // Local installed version from Android Studio
        local(file("/Applications/Android Studio.app"))
        // Plugin version from Intellij Marketplace https://plugins.jetbrains.com/plugin/22989-android, specify only if
        // targeting a non Android Studio IDE
        // IMPORTANT: Plugin versions must not be higher than the Android Studio version.
        // Examples:
        // - Plugin 243.21565.214 (IntelliJ "2024.3") is compatible with Android Studio "2024.3.1.13".
        // - Plugin 243.22562.59 (IntelliJ "2024.3.1 — 2024.3.4.1") is too high for Android Studio "2024.3.1.13"
        //   and will cause errors.
        plugin("org.jetbrains.android:243.21565.214")
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
