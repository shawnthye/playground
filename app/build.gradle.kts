plugins {
    android
    kapt
    firebaseCrashlytics
    navigationSafeArgs
    hilt
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
            signingConfig = null
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
                nativeSymbolUploadEnabled = false
            }
        }
        release {
            signingConfig = null
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

    api(platformDepConstraints)
    kapt(platformDepConstraints)
    testImplementation(platformDepConstraints)
    testRuntimeOnly(platformDepConstraints)

    implementation(project(":core"))
    implementation(project(":core-data"))
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
    implementation(Libs.AndroidX.Compose.uiToolingPreview)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    debugImplementation(Libs.AndroidX.Compose.uiTooling)

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

    testImplementation(platform(Libs.Test.Junit.bom))
    testImplementation(Libs.Test.Junit.jupiterApi)
    testRuntimeOnly(Libs.Test.Junit.jupiterEngine)
    testImplementation(Libs.Test.Hamcrest.library)
    testImplementation(Libs.Test.mockk)

    "internalImplementation"(Libs.Google.Cronet.cronet)
    "internalImplementation"(Libs.Google.Cronet.cronetFallback)
}

apply(plugin = "com.google.gms.google-services")
