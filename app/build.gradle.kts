import android.annotation.SuppressLint

plugins {
    id("com.android.application")
    // id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    jacoco
    // id("com.google.devtools.ksp") version "1.5.31-1.0.0"
    // id("idea")

    // id("androidx.navigation.safeargs.kotlin")
    // id("com.google.firebase.crashlytics")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        applicationId = "app.playground"
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf(
            "runnerBuilder" to "de.mannodermaus.junit5.AndroidJUnit5Builder",
            "clearPackageData" to "true",
        )

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                    "room.expandProjection" to "true",
                )
            }
        }
    }

    flavorDimensions += "default"

    productFlavors {
        maybeCreate("internal")
        maybeCreate("production")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro",
            )
        }
        debug {
            isTestCoverageEnabled = true
            versionNameSuffix = "-debug"
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true

        // false positive, gradle lint only recognize kotlin("kotlin-kapt")
        @SuppressLint("DataBindingWithoutKapt")
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Exclude AndroidX version files
            excludes += "META-INF/*.version"
            // Exclude consumer proguard files
            excludes += "META-INF/proguard/*"
            // Exclude the Firebase/Fabric/other random properties files
            excludes += "/*.properties"
            excludes += "fabric/*.properties"
            excludes += "META-INF/*.properties"
        }
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

    implementation(project(":core"))
    implementation(project(":app-entities"))
    implementation(project(":feature-deviant"))

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Coroutines.play)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.activity)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.activityCompose)
    implementation(Libs.material)

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Lifecycle.viewModel)

    implementation(Libs.Dagger.dagger)
    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.AndroidX.Room.ktx)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)
    implementation(Libs.AndroidX.Compose.uiTestJunit4)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    implementation(platform(Libs.Firebase.bom))
    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.config)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)
    implementation(Libs.OkHttp3.logging)

    implementation(Libs.Retrofit2.retrofit)
    implementation(Libs.Retrofit2.converter)
    implementation(Libs.Kotlin.xJson)

    implementation(Libs.coil)

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
