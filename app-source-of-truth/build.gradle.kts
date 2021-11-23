plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    kapt(platform(project(":build-dep-constraints")))

    implementation(project(":core"))
    implementation(project(":core-data"))
    @Suppress("SpellCheckingInspection")
    implementation(project(":deviantart-api-model"))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.AndroidX.Paging.common)

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)

}
