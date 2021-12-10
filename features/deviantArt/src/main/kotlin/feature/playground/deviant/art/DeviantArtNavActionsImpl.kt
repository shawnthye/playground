package feature.playground.deviant.art

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import app.playground.core.navigation.DeviantArtNavActions
import feature.playground.deviant.art.deviation.ui.DeviationDetailFragment

class DeviantArtNavActionsImpl : DeviantArtNavActions {
    override fun toDeviation(navController: NavController, id: String) {
        navController.navigate(
            R.id.deviation_detail,
            bundleOf(DeviationDetailFragment.ARG_DEVIATION_ID to id),
        )
    }
}
