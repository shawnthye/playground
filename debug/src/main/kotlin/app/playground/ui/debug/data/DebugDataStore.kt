package app.playground.ui.debug.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_COIL_LOGGING
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_ENVIRONMENT
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_OKHTTP_LOGGING
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_SEEN_DRAWER
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
import kotlin.system.exitProcess
import okhttp3.logging.HttpLoggingInterceptor.Level as HttpLogingLevel

internal val Context.debugDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "DEBUG_DATA_STORE",
)

@Singleton
class DebugStorage @Inject constructor(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    coilLogger: Logger?,
    @ApplicationContext context: Context,
    @ApplicationScope applicationScope: CoroutineScope,
) {

    private val store = context.debugDataStore

    private object PreferencesKeys {
        val PREF_SEEN_DRAWER = booleanPreferencesKey("pref_seen_drawer")
        val PREF_ENVIRONMENT = stringPreferencesKey("pref_environment")
        val PREF_OKHTTP_LOGGING = stringPreferencesKey("pref_okhttp_logging")
        val PREF_COIL_LOGGING = stringPreferencesKey("pref_coil_logging")
    }

    object Defaults {
        val Environment = DebugEnvironment.PRODUCTION
        val OkhttpLoggingLevel = HttpLogingLevel.NONE
        val CoilLoggingLevel = CoilLogLevel.NONE
    }

    suspend fun clear() {
        store.edit { it.clear() }
    }

    val seenDrawer: Flow<Boolean> = store.data.map {
        it[PREF_SEEN_DRAWER] ?: false
    }.distinctUntilChanged()

    suspend fun seenDrawer() {
        store.edit { it[PREF_SEEN_DRAWER] = true }
    }

    val environment: Flow<DebugEnvironment> = store.data.map {
        DebugEnvironment
            .values()
            .find { env ->
                env.name == it[PREF_ENVIRONMENT]
            } ?: Defaults.Environment
    }.distinctUntilChanged()

    suspend fun environment(environment: DebugEnvironment) {
        store.edit { it[PREF_ENVIRONMENT] = environment.name }
    }

    val httpLoggingLevel: Flow<HttpLogingLevel> = store.data.map {
        HttpLogingLevel
            .values()
            .find { level ->
                level.name == it[PREF_OKHTTP_LOGGING]
            } ?: Defaults.OkhttpLoggingLevel
    }.distinctUntilChanged()

    suspend fun httpLoggingLevel(level: HttpLogingLevel) {
        store.edit { it[PREF_OKHTTP_LOGGING] = level.name }
    }

    val coilLogging: Flow<CoilLogLevel> = store.data.map {
        CoilLogLevel
            .values()
            .find { level ->
                level.name == it[PREF_COIL_LOGGING]
            } ?: Defaults.CoilLoggingLevel
    }.distinctUntilChanged()

    suspend fun coilLogging(level: CoilLogLevel) {
        store.edit { it[PREF_COIL_LOGGING] = level.name }
    }

    init {
        environment.onEach {
            Timber.i("debug environment = $it")
        }.launchIn(applicationScope)

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
