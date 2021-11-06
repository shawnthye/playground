plugins {
    id("kotlin")
    // id("kotlin-kapt")
    id("com.android.lint")
}

dependencies {
    api(platform(project(":dep-constraints")))

    implementation(project(mapOf("path" to ":deviantart-api-model")))
    implementation(project(":core-data"))

    implementation(Libs.Coroutines.core)

    implementation(Libs.Dagger.javaxInject)
    // implementation(Libs.Dagger.dagger)
    // kapt(Libs.Dagger.daggerCompiler)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)

    implementation(Libs.Retrofit2.retrofit)
    implementation(Libs.Retrofit2.retrofitConverterGson)

    implementation(Libs.Dagger.dagger)
}
