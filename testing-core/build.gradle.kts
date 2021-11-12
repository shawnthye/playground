plugins {
    kotlin("jvm")
    `java-library`
    id("com.android.lint")
}

dependencies {
    testImplementation(platform(project(":build-dep-constraints")))

    testImplementation(platform(Libs.Junit5.bom))
    testImplementation(Libs.Junit5.jupiterApi)
    testImplementation(Libs.Coroutines.core)
    testImplementation(Libs.Coroutines.test)
}
