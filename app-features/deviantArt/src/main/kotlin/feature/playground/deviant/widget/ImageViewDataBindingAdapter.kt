package feature.playground.deviant.widget

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.repeatCount

@BindingAdapter(value = ["imageUrl", "placeholder", "crossFade"], requireAll = false)
fun imageUrl(
    imageView: ImageView,
    imageUrl: String?,
    placeholder: Drawable?,
    crossFade: Boolean?,
) {
    imageUri(imageView, imageUrl?.toUri(), placeholder, crossFade)
}

@BindingAdapter(value = ["imageUri", "placeholder", "crossFade"], requireAll = false)
fun imageUri(
    imageView: ImageView,
    imageUri: Uri?,
    placeholder: Drawable?,
    crossFade: Boolean?,
) {

    val crossFadeEnabled = crossFade ?: true

    when (imageUri) {
        null -> {
            imageView.load(drawable = placeholder) {
                if (!crossFadeEnabled) {
                    crossfade(enable = false)
                }
            }
        }
        else -> {
            imageView.load(uri = imageUri) {
                placeholder(drawable = placeholder)
                if (!crossFadeEnabled) {
                    crossfade(enable = false)
                }
                repeatCount(0)
            }
        }
    }
}
