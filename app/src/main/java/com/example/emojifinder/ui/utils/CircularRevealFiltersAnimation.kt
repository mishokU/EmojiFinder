package com.example.emojifinder.ui.utils

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView


fun openFilters(
    shopRecyclerView: RecyclerView,
    filtersPlace: RelativeLayout,
    xOffset : Int, radiusOffset : Int
) {
    val x: Int = shopRecyclerView.x.toInt() + shopRecyclerView.width - xOffset
    val y: Int = 0

    val startRadius = 0
    val endRadius: Int = filtersPlace.width + radiusOffset

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
    filtersPlace: RelativeLayout,
    xOffset : Int, radiusOffset : Int
) {
    val x: Int = shopRecyclerView.x.toInt() + shopRecyclerView.width - xOffset
    val y: Int = 0

    val startRadius: Int = filtersPlace.width + radiusOffset
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

fun openChestPlaceholder(
    view: View,
    openChestPlaceholder: View
) {
    val x: Int = view.x.toInt() + view.width / 2
    val y: Int = view.y.toInt()

    val startRadius = 0
    val endRadius: Int = openChestPlaceholder.width

    val anim = ViewAnimationUtils.createCircularReveal(
        openChestPlaceholder, x, y,
        startRadius.toFloat(),
        endRadius.toFloat()
    )

    anim.duration = 1000
    openChestPlaceholder.visibility = View.VISIBLE
    anim.start()
}

fun closeChestPlaceholder(
    shopRecyclerView: RelativeLayout,
    filtersPlace: View
) {
    val x: Int = shopRecyclerView.x.toInt() + shopRecyclerView.width / 2 - 50
    val y: Int = shopRecyclerView.y.toInt()

    val startRadius: Int = filtersPlace.width / 2
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

