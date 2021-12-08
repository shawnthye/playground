plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
    id("com.android.lint")
    // jacoco
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    compileOnly(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    implementation(project(":core"))
    implementation(project(":core-data"))

    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.Coroutines.core)

    compileOnly(Libs.Apollo.runtime)
    api(Libs.Apollo.coroutines)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)
}

apollo {
    generateKotlinModels.set(true)
}
