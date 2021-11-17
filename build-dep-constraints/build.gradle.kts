plugins {
    id("java-platform")
}

dependencies {
    constraints {
        api("${Libs.AndroidX.Compose.material}:${Versions.COMPOSE}")
        api("${Libs.AndroidX.Compose.uiTestJunit4}:${Versions.COMPOSE}")
        api("${Libs.AndroidX.Compose.uiToolingPreview}:${Versions.COMPOSE}")
        api("${Libs.AndroidX.Compose.uiTooling}:${Versions.COMPOSE}")
        api("${Libs.AndroidX.Compose.ui}:${Versions.COMPOSE}")
        api("${Libs.AndroidX.Lifecycle.runtime}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Lifecycle.runtime}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Lifecycle.viewModelSavedState}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Lifecycle.viewModelSavedState}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Lifecycle.viewModel}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Lifecycle.viewModel}:${Versions.LIFECYCLE}")
        api("${Libs.AndroidX.Navigation.compose}:${Versions.NAVIGATION}")
        api("${Libs.AndroidX.Navigation.fragment}:${Versions.NAVIGATION}")
        api("${Libs.AndroidX.Navigation.ui}:${Versions.NAVIGATION}")
        api("${Libs.AndroidX.Room.common}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.common}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.compiler}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.compiler}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.ktx}:${Versions.ROOM}")
        api("${Libs.AndroidX.Test.coreKtx}:${Versions.ANDROID_TEST_CORE}")
        api("${Libs.AndroidX.Test.core}:${Versions.ANDROID_TEST_CORE}")
        api("${Libs.AndroidX.Test.espresso}:${Versions.ANDROID_TEST_ESPRESSO}")
        api("${Libs.AndroidX.Test.junitKtx}:${Versions.ANDROID_TEST_JUNIT}")
        api("${Libs.AndroidX.Test.junit}:${Versions.ANDROID_TEST_JUNIT}")
        api("${Libs.AndroidX.Test.orchestrator}:${Versions.ANDROID_TEST_ORCHESTRATOR}")
        api("${Libs.AndroidX.Test.rules}:${Versions.ANDROID_TEST_RULES}")
        api("${Libs.AndroidX.Test.runner}:${Versions.ANDROID_TEST_RUNNER}")
        api("${Libs.AndroidX.activityCompose}:${Versions.ACTIVITY}")
        api("${Libs.AndroidX.activityKtx}:${Versions.ACTIVITY}")
        api("${Libs.AndroidX.activity}:${Versions.ACTIVITY}")
        api("${Libs.AndroidX.annotation}:${Versions.ANNOTATION}")
        api("${Libs.AndroidX.appcompat}:${Versions.APPCOMPAT}")
        api("${Libs.AndroidX.concurrent}:${Versions.CONCURRENT}")
        api("${Libs.AndroidX.constraintLayout}:${Versions.CONSTRAINT_LAYOUT}")
        api("${Libs.AndroidX.core}:${Versions.CORE}")
        api("${Libs.AndroidX.customView}:${Versions.CUSTOM_VIEW}")
        api("${Libs.AndroidX.drawerLayout}:${Versions.DRAWER_LAYOUT}")
        api("${Libs.AndroidX.fragmentKtx}:${Versions.FRAGMENT}")
        api("${Libs.AndroidX.fragment}:${Versions.FRAGMENT}")
        api("${Libs.AndroidX.recyclerView}:${Versions.RECYCLER_VIEW}")
        api("${Libs.AndroidX.swipeRefresh}:${Versions.SWIPE_REFRESH}")
        api("${Libs.Coroutines.android}:${Versions.COROUTINES}")
        api("${Libs.Coroutines.core}:${Versions.COROUTINES}")
        api("${Libs.Coroutines.play}:${Versions.COROUTINES}")
        api("${Libs.Coroutines.test}:${Versions.COROUTINES}")
        api("${Libs.Dagger.daggerCompiler}:${Versions.DAGGER}")
        api("${Libs.Dagger.dagger}:${Versions.DAGGER}")
        api("${Libs.Dagger.hiltCompiler}:${Versions.DAGGER}")
        api("${Libs.Dagger.hilt}:${Versions.DAGGER}")
        api("${Libs.Dagger.javaxInject}:${Versions.DAGGER_INJECT}")
        api("${Libs.Firebase.bom}:${Versions.FIREBASE}")
        api("${Libs.Google.Accompanist.insets}:${Versions.ACCOMPANIST}")
        api("${Libs.JetBrains.reflect}:${Versions.KOTLIN}")
        api("${Libs.JetBrains.xJson}:${Versions.KOTLINX_JSON}")
        api("${Libs.Junit5.androidCore}:${Versions.JUNIT5_ANDROID_TEST}")
        api("${Libs.Junit5.androidRunner}:${Versions.JUNIT5_ANDROID_TEST}")
        api("${Libs.Junit5.bom}:${Versions.JUPITER}")
        api("${Libs.OkHttp3.bom}:${Versions.OK_HTTP}")
        api("${Libs.OkHttp3.dnsOverHttp}:${Versions.OK_HTTP}")
        api("${Libs.OkHttp3.okhttp}:${Versions.OK_HTTP}")
        api("${Libs.Retrofit2.converter}:${Versions.RETROFIT_CONVERTER}")
        api("${Libs.Retrofit2.retrofit}:${Versions.RETROFIT}")
        api("${Libs.Test.hamcrestCore}:${Versions.HAMCREST}")
        api("${Libs.Test.hamcrestLibrary}:${Versions.HAMCREST}")
        api("${Libs.Test.junit}:${Versions.JUNIT}")
        api("${Libs.Test.mockk}:${Versions.MOCKK}")
        api("${Libs.coil}:${Versions.COIL}")
        api("${Libs.material}:${Versions.MATERIAL}")
        api("${Libs.okio}:${Versions.OKIO}")
        api("${Libs.timber}:${Versions.TIMBER}")
    }
}
