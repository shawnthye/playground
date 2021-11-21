package feature.playground.deviant.ui.track

import androidx.annotation.StringRes
import feature.playground.deviant.R

enum class Track(@StringRes val titleResId: Int) {
    POPULAR(R.string.deviant_art_track_popular),
    HOT(R.string.deviant_art_track_hot)
}
