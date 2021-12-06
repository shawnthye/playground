package feature.playground.demos.error.domain

import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class OkHttpUseCase @Inject constructor(
    private val okhttp: OkHttpClient,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(coroutineDispatcher) {
    override suspend fun execute(params: Unit) {

        val request = Request.Builder()
            .url("https://www.google.com")
            .build()

        @Suppress("BlockingMethodInNonBlockingContext")
        okhttp.newCall(request).execute()
    }
}
