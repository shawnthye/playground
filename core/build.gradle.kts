plugins {
    kotlin("jvm")
    `java-library`
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    implementation(Libs.Coroutines.core)

    implementation(Libs.Dagger.javaxInject)
    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)

    testImplementation(platform(Libs.Junit5.bom))
    testImplementation(Libs.Junit5.jupiterApi)
    testRuntimeOnly(Libs.Junit5.jupiterEngine)
    testImplementation(Libs.Test.hamcrestLibrary)
}
