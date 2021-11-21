/*
 * Copyright 2019 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package feature.playground.deviant.widget

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import androidx.annotation.IdRes
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringAnimation.ALPHA
import androidx.dynamicanimation.animation.SpringAnimation.TRANSLATION_X
import androidx.dynamicanimation.animation.SpringAnimation.TRANSLATION_Y
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import feature.playground.deviant.R

/**
 * A [RecyclerView.ItemAnimator] that fades & slides newly added items in from a given
 * direction.
 */
open class SlideInItemAnimator @JvmOverloads constructor(
    slideFromEdge: Int = Gravity.BOTTOM, // Default to sliding in upward
    layoutDirection: Int = -1,
) : DefaultItemAnimator() {

    private val slideFromEdge: Int = Gravity.getAbsoluteGravity(slideFromEdge, layoutDirection)
    private val pendingAdds = mutableListOf<RecyclerView.ViewHolder>()
    private val runningAdds = mutableListOf<RecyclerView.ViewHolder>()
    private val pendingMoves = mutableListOf<RecyclerView.ViewHolder>()
    private val runningMoves = mutableListOf<RecyclerView.ViewHolder>()

    @SuppressLint("RtlHardcoded")
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        when (slideFromEdge) {
            Gravity.LEFT -> holder.itemView.translationX = -holder.itemView.width / 3f
            Gravity.TOP -> holder.itemView.translationY = -holder.itemView.height / 3f
            Gravity.RIGHT -> holder.itemView.translationX = holder.itemView.width / 3f
            else // Gravity.BOTTOM
            -> holder.itemView.translationY = holder.itemView.height / 3f
        }
        pendingAdds.add(holder)
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromViewX: Int,
        fromViewY: Int,
        toViewX: Int,
        toViewY: Int,
    ): Boolean {
        holder ?: return false
        val view = holder.itemView
        val fromX = fromViewX + holder.itemView.translationX.toInt()
        val fromY = fromViewY + holder.itemView.translationY.toInt()
        endAnimation(holder)
        val deltaX = toViewX - fromX
        val deltaY = toViewY - fromY
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder)
            return false
        }
        if (deltaX != 0) {
            view.translationX = (-deltaX).toFloat()
        }
        if (deltaY != 0) {
            view.translationY = (-deltaY).toFloat()
        }
        pendingMoves.add(holder)
        return true
    }

    override fun runPendingAnimations() {
        super.runPendingAnimations()
        if (pendingAdds.isNotEmpty()) {
            for (i in pendingAdds.indices.reversed()) {
                addItem(pendingAdds.removeAt(i))
            }
        }

        if (pendingMoves.isNotEmpty()) {
            for (i in pendingMoves.indices.reversed()) {
                moveItem(pendingMoves.removeAt(i))
            }
        }
    }

    override fun endAnimation(holder: RecyclerView.ViewHolder) {
        if (pendingAdds.contains(holder)) endPendingAdd(holder)
        if (runningAdds.contains(holder)) endRunningAdd(holder)
        if (pendingMoves.contains(holder)) endPendingMove(holder)
        if (runningMoves.contains(holder)) endRunningMove(holder)
        super.endAnimation(holder)
    }

    override fun endAnimations() {
        pendingAdds.forEach(::endPendingAdd)
        runningAdds.forEach(::endRunningAdd)
        pendingMoves.forEach(::endPendingMove)
        runningMoves.forEach(::endRunningMove)
        super.endAnimations()
    }

    override fun isRunning() =
        pendingAdds.isNotEmpty() ||
            runningAdds.isNotEmpty() ||
            pendingMoves.isNotEmpty() ||
            runningMoves.isNotEmpty() ||
            super.isRunning()

    private fun addItem(holder: RecyclerView.ViewHolder) {
        val springAlpha = holder.itemView.spring(ALPHA)
        val springTranslationX = holder.itemView.spring(TRANSLATION_X)
        val springTranslationY = holder.itemView.spring(TRANSLATION_Y)
        listenForAllSpringsEnd(
            { cancelled ->
                if (cancelled) {
                    clearAnimatedValues(holder.itemView)
                }
                dispatchAddFinished(holder)
                dispatchFinishedWhenDone()
                runningAdds -= holder
            },
            springAlpha, springTranslationX, springTranslationY,
        )
        springAlpha.animateToFinalPosition(1f)
        springTranslationX.animateToFinalPosition(0f)
        springTranslationY.animateToFinalPosition(0f)
        dispatchAddStarting(holder)
        runningAdds += holder
    }

    private fun moveItem(holder: RecyclerView.ViewHolder) {
        val springX = holder.itemView.spring(TRANSLATION_X)
        val springY = holder.itemView.spring(TRANSLATION_Y)
        listenForAllSpringsEnd(
            { cancelled ->
                if (cancelled) {
                    clearAnimatedValues(holder.itemView)
                }
                dispatchMoveFinished(holder)
                dispatchFinishedWhenDone()
                runningMoves -= holder
            },
            springX, springY,
        )
        springX.animateToFinalPosition(0f)
        springY.animateToFinalPosition(0f)
        dispatchMoveStarting(holder)
        runningMoves += holder
    }

    private fun endPendingAdd(holder: RecyclerView.ViewHolder) {
        clearAnimatedValues(holder.itemView)
        dispatchAddFinished(holder)
        pendingAdds -= holder
    }

    private fun endRunningAdd(holder: RecyclerView.ViewHolder) {
        holder.itemView.spring(ALPHA).cancel()
        holder.itemView.spring(TRANSLATION_X).cancel()
        holder.itemView.spring(TRANSLATION_Y).cancel()
        runningAdds -= holder
    }

    private fun endPendingMove(holder: RecyclerView.ViewHolder) {
        clearAnimatedValues(holder.itemView)
        dispatchMoveFinished(holder)
        pendingMoves -= holder
    }

    private fun endRunningMove(holder: RecyclerView.ViewHolder) {
        holder.itemView.spring(TRANSLATION_X).cancel()
        holder.itemView.spring(TRANSLATION_Y).cancel()
        runningMoves -= holder
    }

    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    private fun clearAnimatedValues(view: View) {
        view.alpha = 1f
        view.translationX = 0f
        view.translationY = 0f
    }
}

/**
 * An extension function which creates/retrieves a [SpringAnimation] and stores it in the [View]s
 * tag.
 */
fun View.spring(
    property: ViewProperty,
    stiffness: Float = 500f,
    damping: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY,
    startVelocity: Float? = null,
): SpringAnimation {
    val key = getKey(property)
    var springAnim = getTag(key) as? SpringAnimation?
    if (springAnim == null) {
        springAnim = SpringAnimation(this, property)
        setTag(key, springAnim)
    }
    springAnim.spring = (springAnim.spring ?: SpringForce()).apply {
        this.dampingRatio = damping
        this.stiffness = stiffness
    }
    startVelocity?.let { springAnim.setStartVelocity(it) }
    return springAnim
}

/**
 * A class which adds [DynamicAnimation.OnAnimationEndListener]s to the given `springs` and invokes
 * `onEnd` when all have finished.
 */
class MultiSpringEndListener(
    onEnd: (Boolean) -> Unit,
    vararg springs: SpringAnimation,
) {
    private val listeners = ArrayList<DynamicAnimation.OnAnimationEndListener>(springs.size)

    private var wasCancelled = false

    init {
        springs.forEach {
            val listener = object : DynamicAnimation.OnAnimationEndListener {
                override fun onAnimationEnd(
                    animation: DynamicAnimation<out DynamicAnimation<*>>?,
                    canceled: Boolean,
                    value: Float,
                    velocity: Float,
                ) {
                    animation?.removeEndListener(this)
                    wasCancelled = wasCancelled or canceled
                    listeners.remove(this)
                    if (listeners.isEmpty()) {
                        onEnd(wasCancelled)
                    }
                }
            }
            it.addEndListener(listener)
            listeners.add(listener)
        }
    }
}

fun listenForAllSpringsEnd(
    onEnd: (Boolean) -> Unit,
    vararg springs: SpringAnimation,
) = MultiSpringEndListener(onEnd, *springs)

/**
 * Map from a [ViewProperty] to an `id` suitable to use as a [View] tag.
 */
@IdRes
private fun getKey(property: ViewProperty): Int {
    return when (property) {
        TRANSLATION_X -> R.id.translation_x
        TRANSLATION_Y -> R.id.translation_y
        SpringAnimation.TRANSLATION_Z -> R.id.translation_z
        SpringAnimation.SCALE_X -> R.id.scale_x
        SpringAnimation.SCALE_Y -> R.id.scale_y
        SpringAnimation.ROTATION -> R.id.rotation
        SpringAnimation.ROTATION_X -> R.id.rotation_x
        SpringAnimation.ROTATION_Y -> R.id.rotation_y
        SpringAnimation.X -> R.id.x
        SpringAnimation.Y -> R.id.y
        SpringAnimation.Z -> R.id.z
        ALPHA -> R.id.alpha
        SpringAnimation.SCROLL_X -> R.id.scroll_x
        SpringAnimation.SCROLL_Y -> R.id.scroll_y
        else -> throw IllegalAccessException("Unknown ViewProperty: $property")
    }
}
