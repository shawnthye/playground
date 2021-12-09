package feature.playground.deviant.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.R

@AndroidEntryPoint
class DeviantArt : AppCompatActivity(), NavigationHost {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.deviant_art)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container,
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        bottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.deviant_track, R.id.deviant_popular_start),
        )

        bottomNavigationView.setOnItemReselectedListener { menu ->
            val destination = navController.graph[menu.itemId]
            val graph = destination as? NavGraph ?: return@setOnItemReselectedListener
            navController.popBackStack(graph.startDestinationId, false)
        }

        /**
         * We apply insets manually,
         * because we don't want ripple effect to reach outside of it bar
         * clipPadding does not work, we wanted the default borderless ripple
         */
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, null)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.bottom_navigation_bar),
        ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {

        /**
         * We only override the behavior when back to home from other menu
         * else we leave it to the default [onBackPressed]
         */
        if (bottomNavigationView.selectedItemId != bottomNavigationView.menu[0].itemId) {
            val previousDestination = navController.previousBackStackEntry?.destination ?: run {
                /**
                 * on the root, leave it to default [onBackPressed]
                 */
                return super.onBackPressed()
            }

            /**
             * in case the first menu/tab is not a nested graph
             */
            val previousId = previousDestination.parent?.id ?: previousDestination.id
            if (previousId == bottomNavigationView.menu[0].itemId) {
                /**
                 * We let [BottomNavigationView] to switch to home
                 */
                bottomNavigationView.selectedItemId = bottomNavigationView.menu[0].itemId
                return
            }
        }

        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
