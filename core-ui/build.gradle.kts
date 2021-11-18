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

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)

    implementation(Libs.AndroidX.core) // TODO: move to Deviant Art also?
    implementation(Libs.AndroidX.appcompat) // TODO: move to Deviant Art
    implementation(Libs.AndroidX.constraintLayout) // TODO: move to Deviant Art
    implementation(Libs.AndroidX.swipeRefresh) // TODO: move to Deviant Art

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)

    implementation(Libs.Coil.coil)  // TODO: move to Deviant Art

    testImplementation(platform(Libs.Junit5.bom))
    testImplementation(Libs.Junit5.jupiterApi)
    testRuntimeOnly(Libs.Junit5.jupiterEngine)
    testImplementation(Libs.Test.hamcrestLibrary)
    testImplementation(Libs.Test.mockk)
}
