plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    jacoco
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    implementation(project(":core"))

    implementation(Libs.timber)

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)

    implementation(Libs.Google.Accompanist.insets)
    implementation(Libs.Google.Accompanist.insetsUi)

    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.swipeRefresh)
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation(Libs.material)

    implementation(Libs.Coil.coil)
    implementation(Libs.Coil.coilGif)

    testImplementation(Libs.AndroidX.Test.coreKtx)
    testImplementation(platform(Libs.Junit5.bom))
    testImplementation(Libs.Junit5.jupiterApi)
    testRuntimeOnly(Libs.Junit5.jupiterEngine)
    testImplementation(Libs.Test.hamcrestLibrary)
    testImplementation(Libs.Test.mockk)
}
