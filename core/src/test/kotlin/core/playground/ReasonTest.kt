package core.playground

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class ReasonTest {

    @Test
    internal fun `Reason#Connection`() {
        val fake = Throwable("fake")
        val reason: Reason = Reason.Connection(fake)

        assertThat(reason.message, `is`("Connection"))
        assertThat(reason.cause, `is`(fake))
    }

    @Test
    internal fun `Reason#HttpError`() {
        val reason: Reason = Reason.HttpError(code = -1, "fake")

        assertThat(reason.message, `is`("fake"))
        assertThat((reason as Reason.HttpError).code, `is`(-1))
    }
}
