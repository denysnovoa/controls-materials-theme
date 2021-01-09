package com.example.materialTheme.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

fun View.expand(startDelay: Long = 0L, autoStart: Boolean = true): AnimatorSet {
    return AnimatorSet().apply {
        pivotY = 0f
        doOnStart { visibility = View.VISIBLE }
        play(fadeInAnimation())
            .with(scaleUpAnimation())
            .after(startDelay)
    }.also {
        if (autoStart) it.start()
    }
}

fun View.reduce(
    startDelay: Long = 0L,
    autoStart: Boolean = true,
    onFinish: () -> Unit = {}
): AnimatorSet {
    return AnimatorSet().apply {
        pivotY = 0f
        doOnEnd {
            visibility = View.GONE
            onFinish()
        }
        play(fadeOutAnimation())
            .with(scaleDownAnimation())
            .after(startDelay)
    }.also {
        if (autoStart) it.start()
    }
}

fun View.fadeInAnimation(
    duration: Long = SCALE_ANIMATION_DURATION
): ObjectAnimator = ObjectAnimator.ofFloat(this, PROPERTY_NAME_ALPHA, 0f, 1f)
    .setDuration(duration)

fun View.fadeOutAnimation(
    duration: Long = SCALE_ANIMATION_DURATION
): ObjectAnimator = ObjectAnimator.ofFloat(this, PROPERTY_NAME_ALPHA, 1f, 0f)
    .setDuration(duration)


private fun View.scaleUpAnimation(): ObjectAnimator = ObjectAnimator
    .ofFloat(this, PROPERTY_NAME_SCALE_Y, 0f, 1f)
    .setDuration(SCALE_ANIMATION_DURATION)

private fun View.scaleDownAnimation(): ObjectAnimator = ObjectAnimator
    .ofFloat(this, PROPERTY_NAME_SCALE_Y, 1f, 0f)
    .setDuration(SCALE_ANIMATION_DURATION)


private const val PROPERTY_NAME_ALPHA = "alpha"
private const val SCALE_ANIMATION_DURATION = 450L
private const val PROPERTY_NAME_SCALE_Y = "scaleY"
