import com.android.playground.buildsrc.ApplicationOptions

plugins {
    id("com.android.library")
    id("kotlin-android")
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
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${app.playground.buildsrc.Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${app.playground.buildsrc.Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${app.playground.buildsrc.Versions.KOTLIN}")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
