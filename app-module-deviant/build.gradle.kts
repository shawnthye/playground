import app.playground.buildsrc.ApplicationOptions
import app.playground.buildsrc.Versions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "app.playground.module.deviant"

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

    implementation(project(mapOf("path" to ":deviantart-api")))

    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":core-data")))
    implementation(project(mapOf("path" to ":core-domain")))
    implementation(project(mapOf("path" to ":core-ui")))

    implementation(project(":app-entities"))

    implementation("com.jakewharton.timber:timber:${Versions.TIMBER}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.KOTLIN}")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("com.google.android.material:material:1.4.0")

    implementation("androidx.room:room-common:2.3.0")

    // Dagger
    implementation("com.google.dagger:hilt-android:${Versions.HILT}")



    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.3.1")




    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
