plugins {
    kotlinJvm
    androidLint
    apollo
}

dependencies {
    api(platformDepConstraints)
    compileOnly(platformDepConstraints)
    testImplementation(platformDepConstraints)
    testRuntimeOnly(platformDepConstraints)

    implementation(project(Libs.Project.core))
    implementation(project(Libs.Project.coreData))

    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.Coroutines.core)

    implementation(Libs.Apollo.runtime)
    api(Libs.Apollo.api)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)
}

apollo {
    packageName.set("api.product.hunt")
}
