plugins {
    androidLibrary
}

android {
    compileSdk = BuildOptions.COMPILE_SDK
    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":build-dep-constraints")))

    // androidTestImplementation(Libs.timber)
    // androidTestImplementation(Libs.AndroidX.Test.runner)
}
