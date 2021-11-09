plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = ApplicationOptions.COMPILE_SDK
    defaultConfig {
        minSdk = ApplicationOptions.MIN_SDK
        targetSdk = ApplicationOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":dep-constraints")))

    androidTestImplementation(Libs.timber)
    androidTestImplementation(Libs.AndroidX.Test.runner)
}
