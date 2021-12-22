plugins {
    kotlinJvm
    androidLint
}

dependencies {
    api(platform(project(Libs.Project.dependencyConstraints)))

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testImplementation(Libs.Coroutines.core)
    testApi(Libs.Coroutines.test)
}
