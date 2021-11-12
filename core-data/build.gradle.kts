plugins {
    kotlin("jvm")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    implementation(Libs.AndroidX.annotation)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Retrofit2.retrofit)
}
