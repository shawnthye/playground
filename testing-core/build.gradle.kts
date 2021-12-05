plugins {
    kotlin("jvm")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    implementation(platform(Libs.Junit5.bom))
    implementation(Libs.Junit5.jupiterApi)
    implementation(Libs.Coroutines.core)
    api(Libs.Coroutines.test)
}
