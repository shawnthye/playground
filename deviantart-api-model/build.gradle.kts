plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}
