plugins {
    androidLibrary
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
}

dependencies {
    api(platformDepConstraints)

    implementation(project(Libs.Project.core))
    implementation(project(Libs.Project.coreData))
    implementation(project(Libs.Project.deviantArtModel))
    compileOnly(project(Libs.Project.productHuntApi))

    implementation(Libs.Coroutines.core)
    implementation(Libs.Dagger.javaxInject)

    implementation(Libs.AndroidX.Paging.common)

    implementation(Libs.AndroidX.Room.common)
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.paging)
}
