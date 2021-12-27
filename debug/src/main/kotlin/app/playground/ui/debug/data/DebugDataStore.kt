package app.playground.ui.debug.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_COIL_LOGGING
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_ENVIRONMENT
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_NETWORK_HTTP_ENGINE
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_NETWORK_OKHTTP_LOGGING
import app.playground.ui.debug.data.DebugStorage.PreferencesKeys.PREF_SEEN_DRAWER
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

internal val Context.debugDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "DEBUG_DATA_STORE",
)

@Singleton
class DebugStorage @Inject constructor(@ApplicationContext context: Context) {

    private val store = context.debugDataStore

    private object PreferencesKeys {
        val PREF_SEEN_DRAWER = booleanPreferencesKey("pref_seen_drawer")
        val PREF_ENVIRONMENT = stringPreferencesKey("pref_environment")
        val PREF_NETWORK_HTTP_ENGINE = stringPreferencesKey("pref_network_http_engine")
        val PREF_NETWORK_OKHTTP_LOGGING = stringPreferencesKey("pref_network_okhttp_logging")
        val PREF_COIL_LOGGING = stringPreferencesKey("pref_coil_logging")
    }

    internal object Defaults {
        val Environment = DebugEnvironment.PRODUCTION
        val NetworkHttpEngine = HttpEngine.OKHTTP
        val OkhttpLoggingLevel = HttpLogging.NONE
        val CoilLoggingLevel = CoilLogLevel.NONE
    }

    internal suspend fun clear() {
        store.edit { it.clear() }
    }

    internal val seenDrawer: Flow<Boolean> = store.data.map {
        it[PREF_SEEN_DRAWER] ?: false
    }.distinctUntilChanged()

    internal suspend fun seenDrawer() {
        store.edit { it[PREF_SEEN_DRAWER] = true }
    }

    val environment: Flow<DebugEnvironment> = store.data.map {
        DebugEnvironment
            .values()
            .find { env ->
                env.name == it[PREF_ENVIRONMENT]
            } ?: Defaults.Environment
    }.distinctUntilChanged()

    internal suspend fun environment(environment: DebugEnvironment) {
        store.edit { it[PREF_ENVIRONMENT] = environment.name }
    }

    val networkHttpEngine: Flow<HttpEngine> = store.data.map {
        HttpEngine
            .values()
            .find { env ->
                env.name == it[PREF_NETWORK_HTTP_ENGINE]
            } ?: Defaults.NetworkHttpEngine
    }.distinctUntilChanged()

    internal suspend fun networkHttpEngine(engine: HttpEngine) {
        store.edit { it[PREF_NETWORK_HTTP_ENGINE] = engine.name }
    }

    val httpLoggingLevel: Flow<HttpLogging> = store.data.map {
        HttpLogging
            .values()
            .find { level ->
                level.name == it[PREF_NETWORK_OKHTTP_LOGGING]
            } ?: Defaults.OkhttpLoggingLevel
    }.distinctUntilChanged()

    internal suspend fun httpLoggingLevel(level: HttpLogging) {
        store.edit { it[PREF_NETWORK_OKHTTP_LOGGING] = level.name }
    }

    val coilLogging: Flow<CoilLogLevel> = store.data.map {
        CoilLogLevel
            .values()
            .find { level ->
                level.name == it[PREF_COIL_LOGGING]
            } ?: Defaults.CoilLoggingLevel
    }.distinctUntilChanged()

    internal suspend fun coilLogging(level: CoilLogLevel) {
        store.edit { it[PREF_COIL_LOGGING] = level.name }
    }
}
