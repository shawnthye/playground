plugins {
    kotlin("jvm")
    `java-library`
    id("com.android.lint")
    jacoco
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    implementation(project(":deviantart-api-model"))
    implementation(project(":core-data"))

    implementation(Libs.Coroutines.core)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)

    implementation(Libs.JetBrains.xJson)

    implementation(Libs.Retrofit2.retrofit)

    implementation(Libs.Dagger.javaxInject)

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testRuntimeOnly(Libs.Test.Junit.jupiterEngine)
    testImplementation(Libs.Test.Hamcrest.library)
    testImplementation(Libs.Test.mockk)
}
