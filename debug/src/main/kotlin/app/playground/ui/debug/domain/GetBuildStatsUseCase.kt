package app.playground.ui.debug.domain

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import app.playground.ui.debug.BuildConfig
import core.playground.MainImmediateDispatcher
import core.playground.domain.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GetBuildStatsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainImmediateDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Map<String, String>>(dispatcher) {

    override suspend fun execute(params: Unit): Map<String, String> = context.buildStats
}

private val Context.buildStats: Map<String, String>
    get() {
        val info = packageManager.getPackageInfo(packageName, 0)
        return mapOf(
            "Name" to info.versionName,
            "Code" to "${PackageInfoCompat.getLongVersionCode(info)}",
            "Type" to BuildConfig.BUILD_TYPE,
        )
    }
