import app.playground.buildsrc.Libs
import app.playground.buildsrc.Versions

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
        api("${Libs.AndroidX.Navigation.fragment}:${Versions.NAVIGATION}")
        api("${Libs.AndroidX.Navigation.ui}:${Versions.NAVIGATION}")
        api("${Libs.AndroidX.Room.common}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.common}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.compiler}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.compiler}:${Versions.ROOM}")
        api("${Libs.AndroidX.Room.ktx}:${Versions.ROOM}")
        api("${Libs.AndroidX.Test.core}:${Versions.ANDROID_TEST_CORE}")
        api("${Libs.AndroidX.Test.espresso}:${Versions.ANDROID_TEST_ESPRESSO}")
        api("${Libs.AndroidX.Test.junit}:${Versions.ANDROID_TEST_JUNIT}")
        api("${Libs.AndroidX.activityCompose}:${Versions.ACTIVITY}")
        api("${Libs.AndroidX.activity}:${Versions.ACTIVITY}")
        api("${Libs.AndroidX.appcompat}:${Versions.APPCOMPAT}")
        api("${Libs.AndroidX.constraintLayout}:${Versions.CONSTRAINT_LAYOUT}")
        api("${Libs.AndroidX.core}:${Versions.CORE}")
        api("${Libs.AndroidX.fragment}:${Versions.FRAGMENT}")
        api("${Libs.Coroutines.android}:${Versions.COROUTINES}")
        api("${Libs.Coroutines.core}:${Versions.COROUTINES}")
        api("${Libs.Coroutines.play}:${Versions.COROUTINES}")
        api("${Libs.Dagger.dagger}:${Versions.DAGGER}")
        api("${Libs.Dagger.hiltCompiler}:${Versions.DAGGER}")
        api("${Libs.Dagger.hilt}:${Versions.DAGGER}")
        api("${Libs.Dagger.hilt}:${Versions.DAGGER}")
        api("${Libs.Firebase.bom}:${Versions.FIREBASE}")
        api("${Libs.OkHttp3.bom}:${Versions.OK_HTTP}")
        api("${Libs.Retrofit2.retrofitConverterGson}:${Versions.RETROFIT}")
        api("${Libs.Retrofit2.retrofit}:${Versions.RETROFIT}")
        api("${Libs.Test.hamcrest}:${Versions.HAMCREST}")
        api("${Libs.Test.junit}:${Versions.JUNIT}")
        api("${Libs.Test.mockk}:${Versions.MOCKK}")
        api("${Libs.coil}:${Versions.COIL}")
        api("${Libs.timber}:${Versions.TIMBER}")
    }
}
