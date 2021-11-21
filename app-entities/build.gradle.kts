plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    kapt(platform(project(":build-dep-constraints")))

    implementation(project(":core-data"))
    @Suppress("SpellCheckingInspection")
    implementation(project(":deviantart-api-model"))

    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}
