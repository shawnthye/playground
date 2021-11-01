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


include(":app")
include(":app-entities")
include(":app-module-deviant")
include(":deviantart-api")
include(":deviantart-api-model")
include(":core")
include(":core-data")
include(":core-domain")
include(":core-ui")
include(":model")
