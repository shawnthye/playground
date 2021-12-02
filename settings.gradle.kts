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
