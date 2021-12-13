package app.playground.di

import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.Server
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class InternalApiModule {

    fun providesServer(storage: DebugStorage): Server {
        val server = runBlocking {
            storage.httpServer.first()
        }

        return server
    }
}
