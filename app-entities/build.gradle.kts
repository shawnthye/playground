plugins {
    id("kotlin")
    id("kotlin-kapt")
    id("com.android.lint")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = emptyList()

dependencies {
    api(platform(project(":dep-constraints")))
    kapt(platform(project(":dep-constraints")))

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)
}
