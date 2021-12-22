import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.`java-library`
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.android: Unit
    get() {
        id("com.android.application")
        kotlin("android")
    }

inline val PluginDependenciesSpec.androidLibrary: Unit
    get() {
        id("com.android.library")
        kotlin("android")
    }

inline val PluginDependenciesSpec.kotlinJvm: Unit
    get() {
        `java-library`
        kotlin("jvm")
    }

inline val PluginDependenciesSpec.androidLint: PluginDependencySpec
    get() = id("com.android.lint")

inline val PluginDependenciesSpec.ksp: PluginDependencySpec
    get() = id("com.google.devtools.ksp")

inline val PluginDependenciesSpec.kapt: PluginDependencySpec
    get() = kotlin("kapt")

inline val PluginDependenciesSpec.hilt: PluginDependencySpec
    get() = id("dagger.hilt.android.plugin")

inline val PluginDependenciesSpec.firebaseCrashlytics: PluginDependencySpec
    get() = id("com.google.firebase.crashlytics")

inline val PluginDependenciesSpec.navigationSafeArgs: PluginDependencySpec
    get() = id("androidx.navigation.safeargs.kotlin")

inline val PluginDependenciesSpec.kotlinxSerialization: PluginDependencySpec
    get() = kotlin("plugin.serialization")

inline val PluginDependenciesSpec.apollo: PluginDependencySpec
    get() = id("com.apollographql.apollo3")

inline val DependencyHandler.platformDepConstraints: Any
    get() = platform(project(":build-dep-constraints"))
