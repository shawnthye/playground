plugins {
    androidLibrary
    kapt
    jacoco
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK

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
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":build-dep-constraints")))
    testImplementation(platform(project(":build-dep-constraints")))
    testRuntimeOnly(platform(project(":build-dep-constraints")))

    implementation(project(":core"))

    implementation(Libs.timber)

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.VectorDrawable.vectorAnimated)

    implementation(Libs.AndroidX.Compose.compiler)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)
    debugImplementation(Libs.AndroidX.Compose.uiTooling)

    implementation(Libs.Google.Accompanist.insets)
    implementation(Libs.Google.Accompanist.insetsUi)

    implementation(Libs.Coil.coilGif)

    // data binding
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.constraintLayout)

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testRuntimeOnly(Libs.Test.Junit.jupiterEngine)
    testRuntimeOnly(Libs.Test.Junit.vintageEngine)

    testImplementation(Libs.Test.Junit.junit4)
    testImplementation(Libs.Test.robolectric)
    testImplementation(Libs.AndroidX.Test.core)
    testImplementation(Libs.AndroidX.Test.junit)

    testImplementation(Libs.Test.Hamcrest.library)
    testImplementation(Libs.Test.mockk)
}
