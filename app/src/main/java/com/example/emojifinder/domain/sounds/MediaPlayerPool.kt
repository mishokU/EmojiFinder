package com.example.emojifinder.domain.sounds

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.emojifinder.R
import com.example.emojifinder.domain.prefs.SettingsPrefs
import javax.inject.Inject

enum class MusicType {SUCCESSFUL, FAIL, WIN, LOSE, BUY, MONEY }

class MediaPlayerPool @Inject constructor(application: Application) {

    private var context: Context = application.applicationContext

    lateinit var backgroundPlayer: MediaPlayer

    private lateinit var winPlayer : MediaPlayer
    private lateinit var losePlayer : MediaPlayer
    private lateinit var gamePlayer : MediaPlayer
    private lateinit var buyEmojiPlayer : MediaPlayer
    private lateinit var failGeneration : MediaPlayer
    private lateinit var successfulGenerateEmoji : MediaPlayer

    var settingsPrefs: SettingsPrefs = SettingsPrefs(application)

    init {
        createPlayers()
    }

    fun createPlayers() {
        if(settingsPrefs.isPlayMusic()){
            backgroundPlayer = MediaPlayer.create(context, R.raw.background_loop)
        }
        successfulGenerateEmoji = MediaPlayer.create(context, R.raw.good_generate)
        winPlayer = MediaPlayer.create(context, R.raw.level_win)
        losePlayer = MediaPlayer.create(context, R.raw.lost_game)
        failGeneration = MediaPlayer.create(context, R.raw.lost_game)
    }

    fun playBackground() {
        if(settingsPrefs.isPlayMusic()) {
            backgroundPlayer.isLooping = true
            backgroundPlayer.start()
        }
    }

    fun pauseBackground() {
        if(settingsPrefs.isPlayMusic()) {
            backgroundPlayer.pause()
        }
    }

    fun resumeBackground() {
        if(settingsPrefs.isPlayMusic()){
            backgroundPlayer.seekTo(backgroundPlayer.currentPosition)
            backgroundPlayer.start()
        }
    }

    fun play(music : MusicType){
        when(music){
            MusicType.SUCCESSFUL -> successfulGenerateEmoji.start()
            MusicType.BUY -> buyEmojiPlayer.start()
            MusicType.FAIL -> failGeneration.start()
            MusicType.LOSE -> losePlayer.start()
            MusicType.WIN -> winPlayer.start()
        }
    }

}