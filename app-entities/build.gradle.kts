plugins {
    id("kotlin")
    id("kotlin-kapt")
    id("com.android.lint")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = emptyList()

dependencies {
    implementation("androidx.room:room-common:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
}
