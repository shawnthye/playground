plugins {
    kotlinJvm
    androidLint
    apollo
}

dependencies {
    api(platform(project(Libs.Project.dependencyConstraints)))
    compileOnly(platform(project(Libs.Project.dependencyConstraints)))
    testImplementation(platform(project(Libs.Project.dependencyConstraints)))
    testRuntimeOnly(platform(project(Libs.Project.dependencyConstraints)))

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
