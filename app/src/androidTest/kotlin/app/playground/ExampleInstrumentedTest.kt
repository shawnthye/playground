package app.playground

import androidx.test.platform.app.InstrumentationRegistry
import app.playground.di.DatabaseModule
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {

    @Test
    @DisplayName("use app context")
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertThat("app.playground", `is`(appContext.packageName))

        val database = DatabaseModule.provideDatabase(appContext)
        assertThat(database, `is`(IsNull.notNullValue()))

        val dao = DatabaseModule.provideDeviationDao(database = database)
        assertThat(dao, `is`(IsNull.notNullValue()))
    }
}
