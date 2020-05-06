package com.example.emojifinder.ui.utils

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object GameDialogs  {

    lateinit var dialogView : View
    lateinit var alert : AlertDialog

    fun showStartDialog(fragment : DaggerFragment){
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setCancelable(false)
        val inflater = fragment.layoutInflater
        dialogView = inflater.inflate(R.layout.start_game_layout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()
        alert.onWindowFocusChanged(false)
    }

    fun showEndGameDialog(fragment : DaggerFragment,statistics: UserLevelStatistics?) {
        val builder = AlertDialog.Builder(fragment.requireContext(), R.style.CustomDialog)
        builder.setCancelable(false)
        val inflater = fragment.layoutInflater

        dialogView = inflater.inflate(R.layout.end_game_layout, null)
        builder.setView(dialogView)
        builder.create()

        val result = dialogView.findViewById<TextView>(R.id.level_result_et)
        val score = dialogView.findViewById<TextView>(R.id.level_result_score)
        val mistakes = dialogView.findViewById<TextView>(R.id.game_result_mistakes)
        val time = dialogView.findViewById<TextView>(R.id.game_result_time)
        val level = dialogView.findViewById<TextView>(R.id.game_result_level)

        if(statistics != null){

            if(statistics.result == "Lost"){
                result.setTextColor(result.resources.getColor(R.color.red_color))
                getNextLevelButton().text = result.resources.getText(R.string.exit)
            } else {
                result.setTextColor(result.resources.getColor(R.color.green_color))
                getNextLevelButton().text = result.resources.getText(R.string.next_level)
            }

            result.text = statistics.result
            score.text = statistics.score.toString()
            mistakes.text = statistics.mistakes.toString()
            time.text = statistics.time
            level.text = statistics.id.toString()
        }

        alert = builder.show()
    }

    fun showExitDialog(fragment : DaggerFragment){
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setCancelable(false)
        val inflater = fragment.layoutInflater

        dialogView = inflater.inflate(R.layout.exit_game_layout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()
    }

    fun getRetryButton() : View {
        return dialogView.findViewById(R.id.try_again_btn)
    }

    fun getNextLevelButton() : MaterialButton {
        return dialogView.findViewById(R.id.next_level_btn)
    }

    fun getUpperBackButton() : MaterialButton{
        return dialogView.findViewById(R.id.upper_back_button)
    }

    fun getUpperRetryButton() : MaterialButton{
        return dialogView.findViewById(R.id.upper_retry_button)
    }

    fun getGameExitButton() : View {
        return dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getStartGameButton() : View {
        return dialogView.findViewById(R.id.start_game_btn)
    }

    fun getResumeGameButton() : View {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}