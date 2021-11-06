object Versions {
    const val ACTIVITY = "1.4.0"
    const val ANDROID_PLUGIN = "7.0.3"
    const val ANDROID_TEST_CORE = "+"
    const val ANDROID_TEST_ESPRESSO = "3.4.0"
    const val ANDROID_TEST_JUNIT = "1.1.3"
    const val APPCOMPAT = "1.3.1"
    const val COIL = "1.4.0"
    const val COMPOSE = "1.0.4"
    const val CONSTRAINT_LAYOUT = "2.1.1"
    const val CORE = "1.7.0"
    const val COROUTINES = "1.5.2"
    const val DAGGER = "2.40"
    const val DAGGER_INJECT = "1"
    const val FIREBASE = "28.4.2"
    const val FRAGMENT = "1.3.6"
    const val HAMCREST = "2.2"
    const val JUNIT = "4.13.2"
    const val KOTLIN = "1.5.31"
    const val LIFECYCLE = "2.4.0"
    const val MATERIAL = "1.4.0"
    const val MOCKK = "1.12.0"
    const val NAVIGATION = "2.3.5"
    const val OK_HTTP = "4.9.0"
    const val RETROFIT = "2.9.0"
    const val ROOM = "2.3.0"
    const val TIMBER = "5.0.1"
}

object Libs {
    const val coil = "io.coil-kt:coil"
    const val material = "com.google.android.material:material"

    @Suppress("SpellCheckingInspection")
    const val timber = "com.jakewharton.timber:timber"

    object Android {
        const val pluginGradle = "com.android.tools.build:gradle:${Versions.ANDROID_PLUGIN}"
    }

    object Kotlin {
        const val pluginGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    }

    object AndroidX {
        const val activity = "androidx.activity:activity-ktx"
        const val activityCompose = "androidx.activity:activity-compose"
        const val appcompat = "androidx.appcompat:appcompat"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout"
        const val core = "androidx.core:core-ktx"
        const val fragment = "androidx.fragment:fragment-ktx"

        object Compose {
            const val material = "androidx.compose.material:material"
            const val ui = "androidx.compose.ui:ui"
            const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
            const val uiTooling = "androidx.compose.ui:ui-tooling"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        }

        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx"
            const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx"
        }

        object Navigation {
            const val fragment = "androidx.navigation:navigation-fragment-ktx"
            const val ui = "androidx.navigation:navigation-ui-ktx"
        }

        object Room {
            const val common = "androidx.room:room-common"
            const val compiler = "androidx.room:room-compiler"
            const val ktx = "androidx.room:room-ktx"
        }

        object Test {
            const val core = "androidx.test:core"
            const val espresso = "androidx.test.espresso:espresso-core"
            const val junit = "androidx.test.ext:junit"
        }
    }

    object Coroutines {
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
        const val play = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"
    }

    object Dagger {
        const val javaxInject = "javax.inject:javax.inject"
        const val dagger = "com.google.dagger:dagger"
        const val daggerCompiler = "com.google.dagger:dagger-compiler"

        const val hilt = "com.google.dagger:hilt-android"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler"
        const val hiltPluginGradle =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.DAGGER}"
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val bom = "com.google.firebase:firebase-bom"
        const val config = "com.google.firebase:firebase-config-ktx"
    }

    object OkHttp3 {
        const val bom = "com.squareup.okhttp3:okhttp-bom:4.9.0"
        const val logging = "com.squareup.okhttp3:logging-interceptor"
        const val okhttp = "com.squareup.okhttp3:okhttp"
    }

    object Retrofit2 {
        const val retrofit = "com.squareup.retrofit2:retrofit"
        const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson"
    }

    object Test {
        const val hamcrest = "org.hamcrest:hamcrest-library"
        const val junit = "junit:junit"
        const val mockk = "io.mockk:mockk"
    }
}
