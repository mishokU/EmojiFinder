package com.example.emojifinder.ui.utils

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener

class ScaleGesture(c: Context?) : OnTouchListener, OnScaleGestureListener {

    private var view: View? = null
    private val gestureScale: ScaleGestureDetector = ScaleGestureDetector(c, this)
    private var scaleFactor = 1f
    private var inScale = false

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        this.view = view
        gestureScale.onTouchEvent(event)
        return true
    }





    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = if (scaleFactor < 0.75f) 0.75f else scaleFactor // prevent our view from becoming too small //
        scaleFactor = (scaleFactor * 100).toInt().toFloat() / 100 // Change precision to help with jitter when user just rests their fingers //
        view?.scaleX = scaleFactor
        view?.scaleY = scaleFactor

        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        inScale = true
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        inScale = false
    }

}