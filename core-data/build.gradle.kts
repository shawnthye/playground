plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {

    implementation(platform(project(":dep-constraints")))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Retrofit2.retrofit)
}
