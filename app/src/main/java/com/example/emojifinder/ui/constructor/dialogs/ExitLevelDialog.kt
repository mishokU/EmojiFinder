package com.example.emojifinder.ui.constructor.dialogs

import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.ui.baseDialog.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ExitLevelDialog {

    lateinit var dialogView: BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close: MaterialButton

    fun create(fragment: DaggerFragment) {
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.leave_constructor_dialog)
        close()
    }

    fun open() {
        dialogView.show()
    }

    private fun close() {
        close = dialogView.findViewById(R.id.leave_level_btn)
        close.setOnClickListener {
            fragment.findNavController().navigateUp()
            dialogView.dismiss()
        }
    }

    fun getSaveLevelBtn(): MaterialButton {
        return dialogView.findViewById(R.id.save_level_dialog_leave)
    }
}