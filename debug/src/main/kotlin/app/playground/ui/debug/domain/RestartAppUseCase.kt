package app.playground.ui.debug.domain

import android.content.Context
import android.content.Intent
import android.widget.Toast
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlin.system.exitProcess

internal class RestartAppUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(ioDispatcher) {

    override suspend fun execute(params: Unit) = context.restart()
}

private fun Context.restart() {
    packageManager.getLaunchIntentForPackage(packageName)?.let {
        Intent.makeRestartActivityTask(it.component)
    }?.also {
        startActivity(it)

        /**
         * We should never ever do this in Production :)
         */
        exitProcess(0)
    } ?: Toast.makeText(
        this,
        "Missing android.intent.category.LAUNCHER",
        Toast.LENGTH_LONG,
    ).show()
}
