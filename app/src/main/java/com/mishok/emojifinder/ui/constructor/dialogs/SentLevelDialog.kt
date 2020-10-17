package com.mishok.emojifinder.ui.constructor.dialogs

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.EditText
import android.widget.ImageButton
import com.mishok.emojifinder.R
import com.mishok.emojifinder.data.db.local.converter.LevelStatus
import com.mishok.emojifinder.ui.base.BaseDialog
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object SentLevelDialog {

    lateinit var dialogView: BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close: MaterialButton
    private lateinit var image: ImageButton
    private lateinit var imageUri : Uri

    fun create(fragment: DaggerFragment) {
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.sent_level_dialog)

        close = dialogView.findViewById(R.id.cancel_sent_level)
        image = dialogView.findViewById(R.id.level_sent_picture)
        close()
    }

    fun open() {
        dialogView.show()
    }

    fun getNameLabel(): EditText {
        return dialogView.findViewById(R.id.level_name_sent)
    }

    private fun getTimeLabel(): EditText {
        return dialogView.findViewById(R.id.level_constructor_time_sent)
    }

    fun getLevelPicture(): ImageButton {
        return image
    }

    fun isNotEmpty(): Boolean {
        return if (getNameLabel().text.toString() != "" && getTimeLabel().text.toString() != "") {
            true
        } else {
            if (getNameLabel().text.toString() == "") {
                getNameLabel().error = fragment.resources.getString(R.string.level_name_is_empty)
            }
            if (getTimeLabel().text.toString() == "") {
                getTimeLabel().error = fragment.resources.getString(R.string.set_level_time)
            }
            false
        }
    }

    fun setImage(data: Uri?) {
        image.setImageURI(data)
    }

    fun getSmallLevelModel(): SmallLevelModel? {
        if (getTimeLabel().text.toString() != "") {
            return SmallLevelModel(
                id = 0,
                title = getNameLabel().text.toString(),
                time = getTimeLabel().text.toString().toInt(),
                status = LevelStatus.ACCEPTED,
                url = "image/${getNameLabel().text}"
            )
        }
        return null
    }

    private fun close() {
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getSentLevelBtn(): MaterialButton {
        return dialogView.findViewById(R.id.sent_level_dialog)
    }

    fun setLevel(level: SmallLevelModel) {
        getNameLabel().setText(level.title)
        getTimeLabel().setText(level.time.toString())
    }

    fun getImage(): Bitmap? = (image.drawable as BitmapDrawable).bitmap
}