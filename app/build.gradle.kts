plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
    jacoco
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
    }

    signingConfigs {
        create("internal") {
            storeFile = rootDir.resolve(".key-stores/internal.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "debug"
        }
    }

    flavorDimensions += "default"

    productFlavors {
        create("internal") {
            dimension = "default"
            signingConfig = signingConfigs.getByName("internal")
            applicationIdSuffix = ".internal"
            versionNameSuffix = "-internal"
            isDefault = true
        }
        create("production") {
            dimension = "default"
            signingConfig = signingConfigs.getByName("internal")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
                nativeSymbolUploadEnabled = false
            }
        }
        release {
            isMinifyEnabled = true
            isCrunchPngs = false
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
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Exclude AndroidX version files
            excludes += "META-INF/*.version"
            // Exclude consumer proguard files
            // excludes += "META-INF/proguard/*"
            // Exclude the Firebase/Fabric/other random properties files
            excludes += "/*.properties"
            excludes += "fabric/*.properties"
            excludes += "META-INF/*.properties"
            excludes += "DebugProbesKt.bin"
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
    implementation(project(":core-ui"))
    implementation(project(":app-core"))
    implementation(project(":discovery"))
    implementation(project(":post"))
    implementation(project(":productHunt"))

    "internalImplementation"(project(":debug"))

    implementation(Libs.timber)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Coroutines.play)

    implementation(Libs.AndroidX.activity)
    implementation(Libs.AndroidX.activityCompose)

    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelCompose)

    implementation(Libs.Dagger.dagger)
    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    implementation(Libs.AndroidX.Compose.compiler)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)
    implementation(Libs.AndroidX.Compose.uiTestJunit4)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Hilt.navigationCompose)

    implementation(Libs.Google.Accompanist.insets)
    implementation(Libs.Google.Accompanist.navigationAnimation)

    implementation(Libs.AndroidX.Navigation.compose)

    implementation(platform(Libs.Firebase.bom))
    implementation(Libs.Firebase.crashlytics)
    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.config)

    implementation(Libs.JetBrains.xJson)

    implementation(platform(Libs.OkHttp3.bom))
    implementation(Libs.OkHttp3.okhttp)
    implementation(Libs.OkHttp3.logging)

    implementation(Libs.Retrofit2.converter)

    implementation(Libs.Coil.coil)
    implementation(Libs.Coil.coilSvg)
    implementation(Libs.Coil.coilGif)
    implementation(Libs.Coil.coilVideo)
    implementation(Libs.Coil.coilCompose)

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

apply(plugin = "com.google.gms.google-services")
