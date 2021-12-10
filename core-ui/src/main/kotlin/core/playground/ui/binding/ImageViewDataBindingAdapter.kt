package core.playground.ui.binding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.repeatCount

@BindingAdapter(value = ["imageUrl", "placeholder", "crossFade"], requireAll = false)
fun ImageView.imageUrl(
    imageUrl: String?,
    placeholder: Drawable?,
    crossFade: Boolean?,
) {
    imageUri(imageUrl?.toUri(), placeholder, crossFade)
}

@BindingAdapter(value = ["imageUri", "placeholder", "crossFade"], requireAll = false)
fun ImageView.imageUri(
    imageUri: Uri?,
    placeholder: Drawable?,
    crossFade: Boolean?,
) {

    val crossFadeEnabled = crossFade ?: true

    when (imageUri) {
        null -> {
            load(drawable = placeholder) {
                if (!crossFadeEnabled) {
                    crossfade(enable = false)
                }
            }
        }
        else -> {
            load(uri = imageUri) {
                placeholder(drawable = placeholder)
                if (!crossFadeEnabled) {
                    crossfade(enable = false)
                }
                repeatCount(0)
            }
        }
    }
}
