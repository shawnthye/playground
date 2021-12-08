plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
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
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    api(platform(project(":build-dep-constraints")))
    ksp(platform(project(":build-dep-constraints")))
    // kapt(platform(project(":build-dep-constraints")))

    implementation(project(":core"))
    implementation(project(":core-data"))
    @Suppress("SpellCheckingInspection")
    implementation(project(":deviantart-api-model"))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.AndroidX.Paging.common)

    implementation(Libs.AndroidX.Room.common)
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.paging)
    ksp(Libs.AndroidX.Room.compiler)
}
