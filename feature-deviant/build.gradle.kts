import app.playground.buildsrc.ApplicationOptions
import app.playground.buildsrc.Libs

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = ApplicationOptions.COMPILE_SDK

    defaultConfig {
        minSdk = ApplicationOptions.MIN_SDK
        targetSdk = ApplicationOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    implementation(project(mapOf("path" to ":app-entities")))
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":core-data")))
    implementation(project(mapOf("path" to ":core-domain")))
    implementation(project(mapOf("path" to ":core-ui")))
    implementation(project(mapOf("path" to ":deviantart-api")))

    // implementation(Libs.timber)
    //
    // implementation(Libs.Dagger.hilt)
    //
    // implementation(Libs.Coroutines.core)
    // implementation(Libs.Coroutines.android)
    // implementation(Libs.Coroutines.play)
    //
    // implementation(Libs.AndroidX.core)
    // implementation(Libs.AndroidX.appcompat)
    // implementation(Libs.AndroidX.activity)
    // implementation(Libs.AndroidX.fragment)
    //
    // implementation(Libs.AndroidX.Room.common)
    // kapt(Libs.AndroidX.Room.compiler)
    //
    // implementation(Libs.AndroidX.Lifecycle.runtime)
    // implementation(Libs.AndroidX.Lifecycle.viewModel)
    // implementation(Libs.AndroidX.Lifecycle.viewModelSavedState)
    //
    // testImplementation(Libs.Test.junit)
    // testImplementation(Libs.Test.hamcrest)
    // testImplementation(Libs.Test.mockk)
    //
    // androidTestImplementation(Libs.AndroidX.Test.junit)
    // androidTestImplementation(Libs.AndroidX.Test.core)
    // androidTestImplementation(Libs.AndroidX.Test.espresso)
}
