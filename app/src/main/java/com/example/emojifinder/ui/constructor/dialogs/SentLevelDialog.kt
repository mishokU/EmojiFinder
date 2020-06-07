package com.example.emojifinder.ui.constructor.dialogs

import android.app.Dialog
import android.view.Window
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.example.emojifinder.data.db.local.converter.LevelStatus
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object SentLevelDialog {

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
        dialogView.setContentView(R.layout.sent_level_dialog)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

        close()
    }

    fun open(){
        dialogView.show()
    }

    fun getNameLabel() : EditText {
        return dialogView.findViewById(R.id.level_name_sent)
    }

    fun getTimeLabel() : EditText {
        return dialogView.findViewById(R.id.level_constructor_time_sent)
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

    fun getSmallLevelModel(): SmallLevelModel? {
        if(getTimeLabel().text.toString() != ""){
            return SmallLevelModel(
                id = 0,
                title = getNameLabel().text.toString(),
                time = getTimeLabel().text.toString().toInt(),
                status = LevelStatus.ACCEPTED
            )
        }
        return null
    }

    private fun close(){
        close = dialogView.findViewById(R.id.cancel_sent_level)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getSentLevelBtn() : MaterialButton {
        return dialogView.findViewById(R.id.sent_level_dialog)
    }

    fun setLevel(level: SmallLevelModel) {
        getNameLabel().setText(level.title)
        getTimeLabel().setText(level.time.toString())
    }
}