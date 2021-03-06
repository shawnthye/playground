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
        id("com.google.devtools.ksp") version "1.6.10-1.0.2"

        // ktlint
        id("com.diffplug.spotless") version "6.2.0"

        // check outdated dependency
        id("com.github.ben-manes.versions") version "0.41.0"

        // grable build performance
        id("com.osacky.doctor") version "0.7.3"
    }
}

// buildCache {
//     local {
//         directory = File(rootDir, ".gradle/build-cache")
//         removeUnusedEntriesAfterDays = 30
//     }
// }

// dependency constraint
include("build-dep-constraints")

include(":app")
include(":app-core")
include(":app-store")
include(":debug")
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
val featuresDir = File(rootDir, "features")


include(":discovery").also {
    project(":discovery").projectDir = File(featuresDir, "discovery")
}
include(":post").also {
    project(":post").projectDir = File(featuresDir, "post")
}
include(":deviantArt").also {
    project(":deviantArt").projectDir = File(featuresDir, "deviantArt")
}
include(":productHunt").also {
    project(":productHunt").projectDir = File(featuresDir, "productHunt")
}
include(":demos").also {
    project(":demos").projectDir = File(featuresDir, "demos")
}

// for testing purpose
include(":gradle-android-library-configuration-assert")
