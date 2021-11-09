plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    jacoco
}

android {
    compileSdk = ApplicationOptions.COMPILE_SDK

    defaultConfig {
        minSdk = ApplicationOptions.MIN_SDK
        targetSdk = ApplicationOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
        release {
            isJniDebuggable = true
            isTestCoverageEnabled = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":dep-constraints")))
    kapt(platform(project(":dep-constraints")))
    testImplementation(platform(project(":dep-constraints")))
    androidTestImplementation(platform(project(":dep-constraints")))
    androidTestUtil(platform(project(":dep-constraints")))

    implementation(project(":app-entities"))
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

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelSavedState)

    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)

    testImplementation(Libs.Test.junit)
    testImplementation(Libs.Test.hamcrestLibrary)
    testImplementation(Libs.Test.mockk)

    androidTestImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.hamcrestLibrary)
    androidTestImplementation(Libs.AndroidX.Test.junit)
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.espresso)
    androidTestUtil(Libs.AndroidX.Test.orchestrator)
}
