package com.example.emojifinder.ui.utils

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView


fun openFilters(
    shopRecyclerView: RecyclerView,
    filtersPlace: RelativeLayout
) {
    val x: Int = shopRecyclerView.x.toInt() + shopRecyclerView.width - 100
    val y: Int = shopRecyclerView.y.toInt()

    val startRadius = 0
    val endRadius: Int = filtersPlace.width + 200

    val anim = ViewAnimationUtils.createCircularReveal(
        filtersPlace, x, y,
        startRadius.toFloat(),
        endRadius.toFloat()
    )
    anim.duration = 1000
    filtersPlace.visibility = View.VISIBLE
    anim.start()
}

fun closeFilters(
    shopRecyclerView: RecyclerView,
    filtersPlace: RelativeLayout
) {
    val x: Int = shopRecyclerView.x.toInt() + shopRecyclerView.width - 100
    val y: Int = shopRecyclerView.y.toInt()

    val startRadius: Int = filtersPlace.width + 200
    val endRadius = 0

    val anim = ViewAnimationUtils.createCircularReveal(
        filtersPlace, x, y,
        startRadius.toFloat(),
        endRadius.toFloat()
    )

    anim.duration = 1000
    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {}
        override fun onAnimationEnd(animator: Animator) {
            filtersPlace.visibility = View.INVISIBLE
        }

        override fun onAnimationCancel(animator: Animator) {}
        override fun onAnimationRepeat(animator: Animator) {}
    })

    anim.start()
}