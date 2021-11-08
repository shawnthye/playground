package app.playground

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("app.playground", appContext.packageName)

        val database = DatabaseModule.provideDatabase(appContext)
        assertThat(database, `is`(IsNull.notNullValue()))

        val dao = DatabaseModule.provideDeviationDao(database = database)
        assertThat(dao, `is`(IsNull.notNullValue()))
    }
}
