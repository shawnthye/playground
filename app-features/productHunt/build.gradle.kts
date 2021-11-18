plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    // jacoco
    // id("de.mannodermaus.android-junit5")
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf(
            "runnerBuilder" to "de.mannodermaus.junit5.AndroidJUnit5Builder",
            "clearPackageData" to "true",
        )
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
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
    androidTestImplementation(platform(project(":build-dep-constraints")))
    androidTestRuntimeOnly(platform(project(":build-dep-constraints")))
    androidTestUtil(platform(project(":build-dep-constraints")))

    implementation(project(":app-core"))
    implementation(project(":app-entities"))
    implementation(project(":core"))
    implementation(project(":core-data"))
    implementation(project(":core-domain"))
    implementation(project(":core-ui"))

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)
    implementation(Libs.AndroidX.Compose.uiTestJunit4)

    implementation(Libs.Google.Accompanist.insets)

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelSavedState)

    implementation(Libs.AndroidX.Navigation.compose)

    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    testImplementation(platform(Libs.Junit5.bom))
    testImplementation(Libs.Junit5.jupiterApi)
    testRuntimeOnly(Libs.Junit5.jupiterEngine)
    testImplementation(Libs.Test.hamcrestLibrary)
    testImplementation(Libs.Test.mockk)

    androidTestImplementation(platform(Libs.Junit5.bom))
    androidTestImplementation(Libs.Junit5.jupiterApi)
    androidTestRuntimeOnly(Libs.Junit5.jupiterEngine)
    androidTestImplementation(Libs.Junit5.androidCore)
    androidTestRuntimeOnly(Libs.Junit5.androidRunner)
    androidTestImplementation(Libs.Test.hamcrestLibrary)
    androidTestImplementation(Libs.AndroidX.Test.junit)
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.espresso)
    androidTestUtil(Libs.AndroidX.Test.orchestrator)
}
