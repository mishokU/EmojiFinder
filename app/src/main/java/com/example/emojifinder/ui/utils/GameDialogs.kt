package com.example.emojifinder.ui.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.ui.game.GameFragment
import dagger.android.support.DaggerFragment

class GameDialogs(val fragment: GameFragment) {

    lateinit var dialogView : View
    lateinit var alert : AlertDialog
    lateinit var builder : AlertDialog.Builder
    lateinit var inflater : LayoutInflater

    fun showStartDialog(){
        builder = AlertDialog.Builder(fragment.requireContext())
        inflater = fragment.layoutInflater

        createBuilder(R.layout.start_game_layout)
    }

    @SuppressLint("InflateParams")
    private fun createBuilder(gameLayout: Int) {
        dialogView = inflater.inflate(gameLayout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()
    }

    fun showEndGameDialog(statistics: UserLevelStatistics?) {
        builder = AlertDialog.Builder(fragment.requireContext(), R.style.CustomDialog)
        inflater = fragment.layoutInflater

        createBuilder(R.layout.end_game_layout)

        val result = dialogView.findViewById<TextView>(R.id.level_result_et)
        val score = dialogView.findViewById<TextView>(R.id.level_result_score)
        val mistakes = dialogView.findViewById<TextView>(R.id.game_result_mistakes)
        val time = dialogView.findViewById<TextView>(R.id.game_result_time)
        val level = dialogView.findViewById<TextView>(R.id.game_result_level)

        if(statistics != null){

            if(statistics.result == "Lost"){
                result.setTextColor(result.resources.getColor(R.color.red_color))
            } else {
                result.setTextColor(result.resources.getColor(R.color.green_color))
            }

            result.text = statistics.result
            score.text = statistics.score.toString()
            mistakes.text = statistics.mistakes.toString()
            time.text = statistics.time
            level.text = statistics.id.toString()
        }
    }

    @SuppressLint("InflateParams")
    fun showExitDialog(){
        builder = AlertDialog.Builder(fragment.requireContext())
        inflater = fragment.layoutInflater

        createBuilder(R.layout.exit_game_layout)
    }

    fun getRetryButton() : View {
        return dialogView.findViewById(R.id.try_again_btn)
    }

    fun getNextLevelButton() : View {
        return dialogView.findViewById(R.id.next_level_btn)
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