plugins {
    kotlinJvm
    androidLint
}

dependencies {
    api(platformDepConstraints)

    implementation(platform(Libs.Test.Junit.bom))
    implementation(Libs.Test.Junit.jupiterApi)
    implementation(Libs.Coroutines.core)
    api(Libs.Coroutines.test)
}
