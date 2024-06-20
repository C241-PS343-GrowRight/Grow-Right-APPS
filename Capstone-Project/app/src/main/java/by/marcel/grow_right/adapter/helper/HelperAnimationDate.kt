package by.marcel.grow_right.adapter.helper

import android.animation.ObjectAnimator

import android.view.View

fun View.animateVisibilityWithFade(isVisible: Boolean, duration: Long = 400) {
    val targetAlpha = if (isVisible) 1f else 0f
    ObjectAnimator
        .ofFloat(this, View.ALPHA, targetAlpha)
        .setDuration(duration)
        .start()
}
