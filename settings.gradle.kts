rootProject.name = "Playground"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        // google()
    }

    plugins {
        id("com.google.devtools.ksp") version "1.6.0-1.0.1"

        id("de.mannodermaus.android-junit5") version "1.8.0.0"

        // ktlint
        id("com.diffplug.spotless") version "6.0.0"

        // check outdated dependency
        id("com.github.ben-manes.versions") version "0.39.0"

        // grable build performance
        id("com.osacky.doctor") version "0.7.3"
    }
}

buildCache {
    local {
        directory = File(rootDir, ".gradle/build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

// dependency constraint
include("build-dep-constraints")

include(":app")
include(":app-core")
include(":app-source-of-truth")
include(":deviantart-api")
include(":product-hunt-api")
include(":deviantart-api-model")
include(":core")
include(":core-data")
include(":core-domain")
include(":core-ui")
include(":testing-core")
include(":testing-core-android")

// features
include(":deviantArt").also {
    project(":deviantArt").projectDir = File(rootDir, "app-features/deviantArt")
}
include(":productHunt").also {
    project(":productHunt").projectDir = File(rootDir, "app-features/productHunt")
}

// for testing purpose
include(":gradle-android-library-configuration-assert")
