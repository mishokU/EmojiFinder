package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.widget.Switch
import com.example.emojifinder.R
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.main.MainActivity
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ExitGameDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.exit_game_layout)
    }

    fun setMusicSwitcher(settingsPrefs: SettingsPrefs) {
        val switcher = dialogView.findViewById<Switch>(R.id.exit_music_switcher)
        switcher.isChecked = settingsPrefs.isPlayMusic()
        switcher.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                settingsPrefs.changeMusic(isChecked)
                (fragment.requireActivity() as MainActivity).mediaPlayerPool.createPlayers()
                (fragment.requireActivity() as MainActivity).mediaPlayerPool.playBackground()
            } else {
                (fragment.requireActivity() as MainActivity).mediaPlayerPool.pauseBackground()
                settingsPrefs.changeMusic(isChecked)
            }
        }
    }

    fun open(){
        dialogView.show()
    }

    fun getGameExitButton() : MaterialButton {
        return dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getResumeGameButton() : MaterialButton {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}