package com.example.emojifinder.ui.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.emojifinder.R
import dagger.android.support.DaggerFragment

object ErrorDialog {

    lateinit var dialogView : View
    lateinit var alert : AlertDialog

    @SuppressLint("InflateParams")
    fun show(fragment: DaggerFragment, error: String?){
        val builder = AlertDialog.Builder(fragment.requireContext())
        val inflater = fragment.layoutInflater

        dialogView = inflater.inflate(R.layout.error_dialog_layout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()

        setErrorText(error)
        setOkeyButton()
    }

    private fun setOkeyButton() {
        val okeyButton = dialogView.findViewById<View>(R.id.okey_btn)
        okeyButton.setOnClickListener {
            alert.dismiss()
        }
    }

    private fun setErrorText(error: String?) {
        val errorText = dialogView.findViewById<TextView>(R.id.errorText)
        errorText.text = error
    }



}