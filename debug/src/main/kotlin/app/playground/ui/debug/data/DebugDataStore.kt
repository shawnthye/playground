package app.playground.ui.debug.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.playground.ui.debug.data.DebugPreferenceStorage.PreferencesKeys.PREF_COIL_LOGGING
import app.playground.ui.debug.data.DebugPreferenceStorage.PreferencesKeys.PREF_OKHTTP_LOGGING
import coil.Coil
import coil.imageLoader
import coil.util.Logger
import core.playground.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor.Level as HttpLogingLevel

internal val Context.debugDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "DEBUG_DATA_STORE",
)

@Singleton
internal class DebugPreferenceStorage @Inject constructor(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    coilLogger: Logger?,
    @ApplicationContext context: Context,
    @ApplicationScope applicationScope: CoroutineScope,
) {

    private val store = context.debugDataStore

    object PreferencesKeys {
        val PREF_OKHTTP_LOGGING = stringPreferencesKey("pref_okhttp_logging")
        val PREF_COIL_LOGGING = stringPreferencesKey("pref_coil_logging")
    }

    object Defaults {
        val OkhttpLoggingLevel = HttpLogingLevel.HEADERS
        val CoilLoggingLevel = CoilLogLevel.NONE
    }

    suspend fun clear() {
        store.edit { it.clear() }
    }

    val httpLoggingLevel: Flow<HttpLogingLevel> = store.data.map {
        HttpLogingLevel.values().find { level ->
            level.name == it[PREF_OKHTTP_LOGGING]
        } ?: Defaults.OkhttpLoggingLevel
    }.distinctUntilChanged()

    suspend fun httpLoggingLevel(level: HttpLogingLevel) {
        store.edit { it[PREF_OKHTTP_LOGGING] = level.name }
    }

    val coilLogging: Flow<CoilLogLevel> = store.data.map {
        CoilLogLevel.values().find { level -> level.name == it[PREF_COIL_LOGGING] }
            ?: Defaults.CoilLoggingLevel
    }.distinctUntilChanged()

    suspend fun coilLogging(level: CoilLogLevel) {
        store.edit { it[PREF_COIL_LOGGING] = level.name }
    }

    init {
        httpLoggingLevel.onEach {
            Timber.i("httpLoggingInterceptor.level = $it")
            httpLoggingInterceptor.level = it
        }.launchIn(applicationScope)

        coilLogging.onEach {
            Timber.i("coilLogger.level = $it")
            if (it == CoilLogLevel.NONE) {
                Coil.setImageLoader(context.imageLoader.newBuilder().logger(null).build())
            } else {
                coilLogger!!.level = it.level
                Coil.setImageLoader(context.imageLoader.newBuilder().logger(coilLogger).build())
            }
        }.launchIn(applicationScope)
    }
}
