package com.example.emojifinder.ui.constructor.dialogs

import android.app.Dialog
import android.view.Window
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object SaveLevelDialog {

    lateinit var dialogView : Dialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        dialogView.setContentView(R.layout.save_level_dialog)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

        close()
    }

    fun open(){
        dialogView.show()
    }

    private fun close(){
        close = dialogView.findViewById(R.id.cancel_save_level)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getNameLabel() : EditText {
        return dialogView.findViewById(R.id.level_name)
    }

    fun getTimeLabel() : EditText {
        return dialogView.findViewById(R.id.level_constructor_time)
    }

    fun getSaveLevelBtn() : MaterialButton {
        return dialogView.findViewById(R.id.save_level_dialog)
    }

    fun isNotEmpty(): Boolean {
        return if(getNameLabel().text.toString() != "" && getTimeLabel().text.toString() != ""){
            true
        } else {
            if(getNameLabel().text.toString() == ""){
                getNameLabel().error = fragment.resources.getString(R.string.level_name_is_empty)
            }
            if(getTimeLabel().text.toString() == ""){
                getTimeLabel().error = fragment.resources.getString(R.string.set_level_time)
            }
            false
        }
    }

    fun getSmallLevelModel(): SmallLevelModel {
        return SmallLevelModel(
            id = 0,
            title = getNameLabel().text.toString(),
            time = getTimeLabel().text.toString().toInt()
        )
    }
}