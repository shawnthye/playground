plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                    "room.expandProjection" to "true",
                )
            }
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
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(platform(project(":build-dep-constraints")))
    kapt(platform(project(":build-dep-constraints")))

    implementation(project(":app-entities"))

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)

    implementation(Libs.Dagger.dagger)
    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.AndroidX.Room.ktx)
    kapt(Libs.AndroidX.Room.compiler)
}
