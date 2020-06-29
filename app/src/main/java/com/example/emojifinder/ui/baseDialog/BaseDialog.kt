package com.example.emojifinder.ui.baseDialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import com.example.emojifinder.R

var ui_flags: Int = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

open class BaseDialog(context: Context, customDialog: Int) : Dialog(context, customDialog) {

    init {
        setWindowConfig()
    }

    private fun setWindowConfig() {
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
        window!!.navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
        window!!.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window!!.attributes.windowAnimations = android.R.anim.slide_in_left
    }

    override fun show() {
//        window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        //window!!.decorView.systemUiVisibility = ui_flags

        // Show the dialog with NavBar hidden.
        super.show()

        // Set the dialog to focusable again.
        //window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}