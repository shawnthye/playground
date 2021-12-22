package core.playground.ui

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

// TODO: added Junit4 and Vintage engine

@Config(sdk = [23])
@RunWith(AndroidJUnit4::class)
internal class UiMessageTest {

    @Test
    fun application() {
        println(ApplicationProvider.getApplicationContext<Application>().packageName)
    }
}
