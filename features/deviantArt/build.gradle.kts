plugins {
    androidLibrary
    kapt
    hilt
    navigationSafeArgs
    jacoco
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            // isTestCoverageEnabled = true
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":build-dep-constraints")))
    kapt(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    implementation(project(":app-core"))
    implementation(project(":app-store"))
    implementation(project(":core"))
    implementation(project(":core-data"))
    implementation(project(":core-domain"))
    implementation(project(":core-ui"))
    implementation(project(":deviantart-api-model"))
    implementation(project(":deviantart-api"))

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.activityKtx)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.recyclerView)
    implementation(Libs.AndroidX.swipeRefresh)
    implementation(Libs.Google.material)

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelSavedState)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    implementation(Libs.AndroidX.Paging.common)
    implementation(Libs.AndroidX.Paging.runtime)

    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.Coil.coil)
    implementation(Libs.Coil.coilGif)
    implementation(Libs.AndroidX.palette)

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testRuntimeOnly(Libs.Test.Junit.jupiterEngine)
    testImplementation(Libs.Test.Hamcrest.library)
    testImplementation(Libs.Test.mockk)
}
