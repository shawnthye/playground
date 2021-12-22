plugins {
    kotlinJvm
    androidLint
}

dependencies {
    api(platformDepConstraints)

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testImplementation(Libs.Coroutines.core)
    testApi(Libs.Coroutines.test)
}
