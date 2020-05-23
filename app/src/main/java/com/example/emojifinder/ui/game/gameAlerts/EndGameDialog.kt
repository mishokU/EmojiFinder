package com.example.emojifinder.ui.game.gameAlerts

import android.app.Dialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object EndGameDialog {

    lateinit var dialogView : Dialog

    fun showEndGameDialog(fragment : DaggerFragment, statistics: UserLevelStatistics?) {
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        // layout to display
        dialogView.setContentView(R.layout.end_game_layout);

        // set color transparent
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));

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

        dialogView.show()
    }

    fun getRetryButton() : View {
        return dialogView.findViewById(R.id.try_again_btn)
    }

    fun getNextLevelButton() : MaterialButton {
        return dialogView.findViewById(R.id.next_level_btn)
    }

    fun getUpperBackButton() : MaterialButton {
        return dialogView.findViewById(R.id.upper_back_button)
    }

    fun getUpperRetryButton() : MaterialButton {
        return dialogView.findViewById(R.id.upper_retry_button)
    }
}