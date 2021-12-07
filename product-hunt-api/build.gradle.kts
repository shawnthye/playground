plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
    id("com.android.lint")
    // jacoco
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    compileOnly(Libs.Apollo.runtime)
    api(Libs.Apollo.coroutines)
    // implementation(project(":deviantart-api-model"))
    // implementation(project(":core-data"))
    //
    // implementation(Libs.Coroutines.core)
    //
    // implementation(platform(Libs.OkHttp3.bom))
    // implementation(Libs.OkHttp3.okhttp)
    //
    // implementation(Libs.JetBrains.xJson)
    //
    // implementation(Libs.Retrofit2.retrofit)
    //
    implementation(Libs.Dagger.javaxInject)
    //
    // testImplementation(platform(Libs.Junit5.bom))
    // testImplementation(Libs.Junit5.jupiterApi)
    // testRuntimeOnly(Libs.Junit5.jupiterEngine)
    // testImplementation(Libs.Test.hamcrestLibrary)
    // testImplementation(Libs.Test.mockk)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}

apollo {
    generateKotlinModels.set(true)
}
