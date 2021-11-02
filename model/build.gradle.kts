plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {
    // no usage just for KotlinCompile
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${app.playground.buildsrc.Versions.COROUTINES}")
}
