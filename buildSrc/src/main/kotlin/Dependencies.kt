object Versions {
    const val ACCOMPANIST = "0.21.3-beta"
    const val ACTIVITY = "1.4.0"
    const val ANDROID_PLUGIN = "7.0.3"
    const val ANDROID_TEST_CORE = "1.4.0"
    const val ANDROID_TEST_ESPRESSO = "3.4.0"
    const val ANDROID_TEST_JUNIT = "1.1.3"
    const val ANDROID_TEST_ORCHESTRATOR = "1.4.0"
    const val ANDROID_TEST_RULES = "1.4.0"
    const val ANDROID_TEST_RUNNER = "1.4.0"
    const val ANNOTATION = "1.3.0"
    const val APPCOMPAT = "1.4.0"
    const val COIL = "1.4.0"
    const val COMPOSE = "1.1.0-beta04"
    const val CONCURRENT = "1.1.0"
    const val CONSTRAINT_LAYOUT = "2.1.2"
    const val CORE = "1.7.0"
    const val COROUTINES = "1.5.2"
    const val CUSTOM_VIEW = "1.1.0"
    const val DAGGER = "2.40.1"
    const val DAGGER_INJECT = "1"
    const val DRAWER_LAYOUT = "1.1.1"
    const val FIREBASE = "29.0.0"
    const val FRAGMENT = "1.4.0"
    const val HAMCREST = "2.2"
    const val HILT_NAVIGATION_COMPOSE = "1.0.0-beta01"
    const val JACOCO = "0.8.7"
    const val JUNIT = "4.13.2"
    const val JUNIT5_ANDROID_TEST = "1.3.0"
    const val JUPITER = "5.8.1"
    const val KOTLIN = "1.6.0"
    const val KOTLINX_JSON = "1.3.1"
    const val KTLINT = "0.43.0"
    const val LIFECYCLE = "2.4.0"
    const val MATERIAL = "1.4.0"
    const val MOCKK = "1.12.1"
    const val NAVIGATION = "2.4.0-beta02"
    const val OKIO = "3.0.0"
    const val OK_HTTP = "4.9.2"
    const val PAGING = "3.1.0"
    const val PAGING_COMPOSE = "1.0.0-alpha14"
    const val RECYCLER_VIEW = "1.2.1"
    const val RETROFIT = "2.9.0"
    const val RETROFIT_CONVERTER = "0.8.0"
    const val ROOM = "2.3.0"
    const val SWIPE_REFRESH = "1.1.0"
    const val TIMBER = "5.0.1"
}

object Libs {
    const val material = "com.google.android.material:material"
    const val okio = "com.squareup.okio:okio"

    @Suppress("SpellCheckingInspection")
    const val timber = "com.jakewharton.timber:timber"

    object Android {
        const val pluginGradle = "com.android.tools.build:gradle:${Versions.ANDROID_PLUGIN}"
    }

    object JetBrains {
        const val pluginGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val xJson = "org.jetbrains.kotlinx:kotlinx-serialization-json"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
    }

    object Google {
        object Accompanist {
            const val insets = "com.google.accompanist:accompanist-insets"
            const val insetsUi = "com.google.accompanist:accompanist-insets-ui"
            const val navigationAnimation =
                "com.google.accompanist:accompanist-navigation-animation"
        }
    }

    object AndroidX {

        const val activity = "androidx.activity:activity"
        const val activityCompose = "androidx.activity:activity-compose"
        const val activityKtx = "androidx.activity:activity-ktx"
        const val annotation = "androidx.annotation:annotation"
        const val appcompat = "androidx.appcompat:appcompat"
        const val concurrent = "androidx.concurrent:concurrent-futures"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout"
        const val core = "androidx.core:core-ktx"
        const val customView = "androidx.customview:customview"
        const val drawerLayout = "androidx.drawerlayout:drawerlayout"
        const val fragment = "androidx.fragment:fragment"
        const val fragmentKtx = "androidx.fragment:fragment-ktx"
        const val recyclerView = "androidx.recyclerview:recyclerview"

        @Suppress("SpellCheckingInspection")
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout"

        object Compose {
            const val material = "androidx.compose.material:material"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended"
            const val ui = "androidx.compose.ui:ui"
            const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
            const val uiTooling = "androidx.compose.ui:ui-tooling"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        }

        object Hilt {
            const val navigationCompose = "androidx.hilt:hilt-navigation-compose"
        }

        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx"
            const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx"
        }

        object Navigation {
            const val pluginGradle =
                "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
            const val compose = "androidx.navigation:navigation-compose"
            const val fragment = "androidx.navigation:navigation-fragment-ktx"
            const val ui = "androidx.navigation:navigation-ui-ktx"
        }

        object Room {
            const val common = "androidx.room:room-common"
            const val compiler = "androidx.room:room-compiler"
            const val ktx = "androidx.room:room-ktx"
        }

        object Paging {
            const val common = "androidx.paging:paging-common-ktx"
            const val compose = "androidx.paging:paging-compose"
            const val runtime = "androidx.paging:paging-runtime-ktx"
        }

        object Test {
            const val core = "androidx.test:core"
            const val coreKtx = "androidx.test:core-ktx"
            const val espresso = "androidx.test.espresso:espresso-core"
            const val junit = "androidx.test.ext:junit"
            const val junitKtx = "androidx.test.ext:junit-ktx"
            const val orchestrator = "androidx.test:orchestrator"
            const val rules = "androidx.test:rules"
            const val runner = "androidx.test:runner"
        }
    }

    object Coroutines {
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
        const val play = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test"
    }

    object Dagger {
        const val javaxInject = "javax.inject:javax.inject"
        const val dagger = "com.google.dagger:dagger"
        const val daggerCompiler = "com.google.dagger:dagger-compiler"

        const val hiltPluginGradle = "com.google.dagger:hilt-android-gradle-plugin:${
            Versions.DAGGER
        }"
        const val hilt = "com.google.dagger:hilt-android"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler"
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val bom = "com.google.firebase:firebase-bom"
        const val config = "com.google.firebase:firebase-config-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object OkHttp3 {
        const val bom = "com.squareup.okhttp3:okhttp-bom"

        @Suppress("SpellCheckingInspection")
        const val dnsOverHttp = "com.squareup.okhttp3:okhttp-dnsoverhttps"
        const val logging = "com.squareup.okhttp3:logging-interceptor"
        const val okhttp = "com.squareup.okhttp3:okhttp"
    }

    object Retrofit2 {
        const val converter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter"
        const val retrofit = "com.squareup.retrofit2:retrofit"
    }

    object Coil {
        const val coil = "io.coil-kt:coil"
        const val coilSvg = "io.coil-kt:coil-svg"
        const val coilGif = "io.coil-kt:coil-gif"
        const val coilVideo = "io.coil-kt:coil-video"

        const val coilCompose = "io.coil-kt:coil-compose"
    }

    object Test {
        const val hamcrestCore = "org.hamcrest:hamcrest-core"
        const val hamcrestLibrary = "org.hamcrest:hamcrest-library"
        const val junit = "junit:junit"
        const val mockk = "io.mockk:mockk"
    }

    object Junit5 {
        const val androidCore = "de.mannodermaus.junit5:android-test-core"
        const val androidRunner = "de.mannodermaus.junit5:android-test-runner"
        const val bom = "org.junit:junit-bom"
        const val jupiterApi = "org.junit.jupiter:junit-jupiter-api"
        const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine"
        const val jupiterParams = "org.junit.jupiter:junit-jupiter-params"
    }
}
