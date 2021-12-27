package app.playground.core

import android.app.Application
import dagger.multibindings.IntoSet

/**
 * We simple do this with Dagger binding [IntoSet]
 *
 * We should try replace with app startup maybe?
 *
 * @SeeAlso https://developer.android.com/topic/libraries/app-startup#kts
 *
 * For App Startup notes
 *
 * @SeeAlso the advantage of dependencies()
 * https://developer.android.com/topic/libraries/app-startup#implement-initializers
 *
 * @SeeAlso Manually initialize components
 * https://developer.android.com/topic/libraries/app-startup#manual
 *
 * @SeeAlso Manually call component initializers
 * https://developer.android.com/topic/libraries/app-startup#manual-initialization
 */
interface AppInitializer {
    fun init(application: Application)
}
