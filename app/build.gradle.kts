import app.playground.buildsrc.Versions

plugins {
    id("com.android.application")
    // id("dagger.hilt.android.plugin")
    // id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-kapt") // kotlin("kapt") will trigger false positive warning on dataBinding = true

    // id("androidx.navigation.safeargs.kotlin")
    // id("com.google.firebase.crashlytics")
}

android {

    compileSdk = 31

    defaultConfig {
        applicationId = "app.playground"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.KOTLIN}")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    // Jetpack compose
    implementation("androidx.compose.ui:ui:${Versions.COMPOSE}")
    implementation("androidx.compose.material:material:${Versions.COMPOSE}")
    implementation("androidx.compose.material:material:${Versions.COMPOSE}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}")

    // Jetpack navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.4.2"))
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Network
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
