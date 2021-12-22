plugins {
    kotlinJvm
    androidLint
    jacoco
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    implementation(Libs.Coroutines.core)

    implementation(Libs.Dagger.javaxInject)
    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testRuntimeOnly(Libs.Test.Junit.jupiterEngine)
    testImplementation(Libs.Test.Hamcrest.library)
}
