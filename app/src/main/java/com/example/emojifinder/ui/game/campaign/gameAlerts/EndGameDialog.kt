package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

enum class State{LOST, FINISHED}

object EndGameDialog {

    lateinit var dialogView : BaseDialog
    lateinit var fragment: DaggerFragment
    lateinit var exit : AppCompatImageButton

    fun showEndGameDialog(fragment : DaggerFragment, statistics: UserLevelStatistics?, state: State) {
        this.fragment = fragment

        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.end_game_layout);

        val result = dialogView.findViewById<TextView>(R.id.level_result_et)
        val score = dialogView.findViewById<TextView>(R.id.level_result_score)
        val mistakes = dialogView.findViewById<TextView>(R.id.game_result_mistakes)
        val time = dialogView.findViewById<TextView>(R.id.game_result_time)
        val level = dialogView.findViewById<TextView>(R.id.game_result_level)

        if(statistics != null){

            if(statistics.result == result.resources.getString(R.string.game_lost)){
                result.setTextColor(result.resources.getColor(R.color.red_color))
            } else {
                result.setTextColor(result.resources.getColor(R.color.green_color))
            }

            initExitButton()

            if(state != State.LOST){
                exit.visibility = View.INVISIBLE
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
        return dialogView.findViewById(R.id.try_campaign_again_btn)
    }

    fun getNextLevelButton() : View {
        return dialogView.findViewById(R.id.next_campaign_level_btn)
    }

    private fun initExitButton() {
        exit = dialogView.findViewById(R.id.exit_campaign_end_btn)
        exit.setOnClickListener {
            fragment.findNavController().navigateUp()
            Handler().postDelayed({
                dialogView.dismiss()
            }, 100)
        }
    }
}