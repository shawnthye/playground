package app.playground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
