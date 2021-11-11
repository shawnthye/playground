plugins {
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Retrofit2.retrofit)
}
