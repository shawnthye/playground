plugins {
    id("kotlin")
    id("com.android.lint")
    jacoco
}

dependencies {
    api(platform(project(":dep-constraints")))

    implementation(project(":deviantart-api-model"))
    implementation(project(":core-data"))

    implementation(Libs.Coroutines.core)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)

    implementation(Libs.Retrofit2.retrofit)
    implementation(Libs.Retrofit2.retrofitConverterGson)

    implementation(Libs.Dagger.javaxInject)
    implementation(Libs.Dagger.dagger)

    testImplementation(Libs.Test.junit)
    testImplementation(Libs.Test.hamcrest)
    testImplementation(Libs.Test.mockk)
}
