plugins {
    id("kotlin")
    id("com.android.lint")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}
