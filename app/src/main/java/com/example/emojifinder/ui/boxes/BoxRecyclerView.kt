package com.example.emojifinder.ui.boxes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class BoxRecyclerView(context: Context) : RecyclerView(context) {

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        return super.fling(velocityX, velocityY)
    }

    override fun getMinFlingVelocity(): Int {
        return 30
    }
}