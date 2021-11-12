plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    kapt(platform(project(":build-dep-constraints")))

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}
