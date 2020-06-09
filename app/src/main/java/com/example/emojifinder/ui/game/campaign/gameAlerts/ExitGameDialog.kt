package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.app.Dialog
import android.view.Window
import android.widget.Switch
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.ui.main.MainActivity
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ExitGameDialog {

    lateinit var dialogView : Dialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        dialogView.setContentView(R.layout.exit_game_layout)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

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

    fun getMusicSwitcher() : Switch {
        return dialogView.findViewById(R.id.exit_music_switcher)
    }

    fun getGameExitButton() : MaterialButton {
        return dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getResumeGameButton() : MaterialButton {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}