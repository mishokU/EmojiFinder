package com.example.emojifinder.ui.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import dagger.android.support.DaggerFragment

class GameDialogs {

    lateinit var dialogView : View
    lateinit var alert : AlertDialog
    lateinit var builder : AlertDialog.Builder
    lateinit var inflater : LayoutInflater

    fun showStartDialog(fragment: DaggerFragment) : GameDialogs{
        builder = AlertDialog.Builder(fragment.requireContext())
        inflater = fragment.layoutInflater
        createBuilder(R.layout.start_game_layout)
        return this
    }

    @SuppressLint("InflateParams")
    private fun createBuilder(gameLayout: Int) {
        dialogView = inflater.inflate(gameLayout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()
    }

    fun showEndGameDialog(fragment : DaggerFragment, statistics: UserLevelStatistics?) {
        builder = AlertDialog.Builder(fragment.requireContext())
        inflater = fragment.layoutInflater

        dialogView = inflater.inflate(R.layout.end_game_layout, null)
        builder.setView(dialogView)
        builder.create()
        alert = builder.show()

    }

    @SuppressLint("InflateParams")
    fun showExitDialog(fragment: DaggerFragment) : GameDialogs{
        builder = AlertDialog.Builder(fragment.requireContext())
        inflater = fragment.layoutInflater
        createBuilder(R.layout.exit_game_layout)
        return this
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

    fun getResumeGameButton() : View {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}