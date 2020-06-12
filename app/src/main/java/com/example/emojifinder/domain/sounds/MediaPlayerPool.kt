package com.example.emojifinder.domain.sounds

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.emojifinder.R
import com.example.emojifinder.domain.prefs.SettingsPrefs
import javax.inject.Inject

enum class MusicType {SUCCESSFUL, FAIL, WIN, LOSE, BUY, MONEY, CORRECT, GAME }

class MediaPlayerPool @Inject constructor(application: Application) {

    private var context: Context = application.applicationContext

    lateinit var backgroundPlayer: MediaPlayer

    private lateinit var winPlayer : MediaPlayer
    private lateinit var losePlayer : MediaPlayer
    private lateinit var gamePlayer : MediaPlayer
    private lateinit var buyEmojiPlayer : MediaPlayer
    private lateinit var failGeneration : MediaPlayer
    private lateinit var successfulGenerateEmoji : MediaPlayer
    private lateinit var correctClick : MediaPlayer

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
        buyEmojiPlayer = MediaPlayer.create(context, R.raw.level_win)
        correctClick = MediaPlayer.create(context, R.raw.correct_click)
        gamePlayer = MediaPlayer.create(context, R.raw.game_music)
        gamePlayer.isLooping = true
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
            MusicType.SUCCESSFUL -> playCorrect(successfulGenerateEmoji)
            MusicType.BUY -> playCorrect(buyEmojiPlayer)
            MusicType.FAIL -> playCorrect(failGeneration)
            MusicType.LOSE -> playCorrect(losePlayer)
            MusicType.WIN -> playCorrect(winPlayer)
            MusicType.CORRECT -> playCorrect(correctClick)
            MusicType.GAME -> playCorrect(gamePlayer)
        }
    }

    private fun playCorrect(sound: MediaPlayer) {
        if(sound.isPlaying) {
            sound.stop()
            sound.prepare()
            sound.start()
        } else {
            sound.start()
        }
    }

    fun stop(music: MusicType) {
        when(music){
            MusicType.SUCCESSFUL -> stopMusic(successfulGenerateEmoji)
            MusicType.BUY -> stopMusic(buyEmojiPlayer)
            MusicType.FAIL -> stopMusic(failGeneration)
            MusicType.LOSE -> stopMusic(losePlayer)
            MusicType.WIN -> stopMusic(winPlayer)
            MusicType.CORRECT -> stopMusic(correctClick)
            MusicType.GAME -> stopMusic(gamePlayer)
        }
    }

    private fun stopMusic(music: MediaPlayer) {
        music.stop()
        music.prepare()
    }

}