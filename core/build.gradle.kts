plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${app.playground.buildsrc.Versions.KOTLIN}")
    implementation("com.google.dagger:dagger:${app.playground.buildsrc.Versions.HILT}")
}
