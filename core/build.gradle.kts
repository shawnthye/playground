plugins {
    id("kotlin")
    id("com.android.lint")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = emptyList()


dependencies {
    implementation("javax.inject:javax.inject:1")
}
