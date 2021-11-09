plugins {
    id("kotlin")
    id("kotlin-kapt")
    id("com.android.lint")
}

dependencies {
    api(platform(project(":dep-constraints")))
    kapt(platform(project(":dep-constraints")))

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}
