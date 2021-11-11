plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.android.lint")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}

dependencies {
    compileOnly(platform(project(":build-dep-constraints")))

    compileOnly(Libs.Kotlin.xJson)
}
