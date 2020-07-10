package com.example.emojifinder.ui.constructor.dialogs

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.EditText
import android.widget.ImageButton
import com.example.emojifinder.R
import com.example.emojifinder.data.db.local.converter.LevelStatus
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object SaveLevelDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.save_level_dialog)
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

    private fun getTimeLabel() : EditText {
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

    fun getSmallLevelModel(): SmallLevelModel? {
        if(getTimeLabel().text.toString() != ""){
            return SmallLevelModel(
                id = 0,
                title = getNameLabel().text.toString(),
                time = getTimeLabel().text.toString().toInt(),
                status = LevelStatus.SAVED,
                url = ""
            )
        }
        return null
    }

    fun setLevel(level: SmallLevelModel) {
        getNameLabel().setText(level.title)
        getTimeLabel().setText(level.time.toString())
    }
}