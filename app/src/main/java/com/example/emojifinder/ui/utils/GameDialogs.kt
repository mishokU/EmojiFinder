package com.example.emojifinder.ui.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object GameDialogs  {

    lateinit var dialogView : View
    lateinit var alert : AlertDialog

    fun showStartDialog(fragment: DaggerFragment, time: Int){
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setCancelable(false)
        val inflater = fragment.layoutInflater
        dialogView = inflater.inflate(R.layout.start_game_layout, null)
        setGameTime(time)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()
        alert.onWindowFocusChanged(false)
    }

    @SuppressLint("SetTextI18n")
    private fun setGameTime(time: Int) {
        val hint = this.dialogView.findViewById<TextView>(R.id.hintText)
        hint.text = hint.resources.getString(R.string.game_hint) + " " + time + "seconds"
    }

    fun showExitDialog(fragment : DaggerFragment){
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setCancelable(false)
        val inflater = fragment.layoutInflater

        this.dialogView = inflater.inflate(R.layout.exit_game_layout, null)
        builder.setView(this.dialogView)
        builder.create()
        alert = builder.show()
    }

    fun getGameExitButton() : View {
        return this.dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getStartGameButton() : MaterialButton {
        return this.dialogView.findViewById(R.id.start_game_btn)
    }

    fun getResumeGameButton() : View {
        return this.dialogView.findViewById(R.id.resume_game_btn)
    }

}