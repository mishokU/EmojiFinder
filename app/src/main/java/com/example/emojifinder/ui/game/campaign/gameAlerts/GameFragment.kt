package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.addListener
import androidx.emoji.text.EmojiCompat
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.local.converter.toEmojiShopModel
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.FragmentGameBinding
import com.example.emojifinder.domain.adds.INTERSTITIAL_VIDEO_ID
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.domain.prefs.ShowGameHintPrefs
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MusicType
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.GameViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.utils.ScaleGesture
import com.example.emojifinder.ui.utils.ScreenSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject


class GameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GameViewModel

    @Inject
    lateinit var levelViewModelFactory: ViewModelProvider.Factory
    lateinit var levelViewModel: CategoriesViewModel

    @Inject
    lateinit var viewModelFactoryAccount: ViewModelProvider.Factory
    lateinit var viewModelAccount: AccountViewModel

    @Inject
    lateinit var gameHint: ShowGameHintPrefs

    private lateinit var interstitialAd: InterstitialAd

    lateinit var binding: FragmentGameBinding
    private lateinit var animation: ObjectAnimator
    private lateinit var gameKeyboardAdapter: GameKeyBoardRecyclerViewAdapter

    private lateinit var level: SmallLevelModel
    private var levelId: Int = 0
    private lateinit var levels: List<SmallLevelModel>
    private var score = 0

    private var list: List<EmojiShopModel?> = listOf()
    private var levelEditTextList: MutableList<EmojiAppCompatEditText> = mutableListOf()

    private var number: Int = 0
    private var keyboardNumber: Int = 0
    private val MAX_KEYBOARD_EMOJIS = 28

    private val randomList: MutableList<EmojiShopModel?> = mutableListOf()

    @Inject
    lateinit var settingsPrefs: SettingsPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        levelViewModel = injectViewModel(levelViewModelFactory)

        initStatisticEmojis()

        CampaignGameHint.create(this)

        interstitialAd = InterstitialAd(requireContext())

        binding.gameEmojiField.setOnTouchListener(ScaleGesture(requireContext()))

        initAudioButton()
        setMusicSwitcher()

        initGameKeyBoardAdapter()

        getGameCategory()

        startGameDialog()

        initButtons()

        loadGameLevel()

        loadUserScore()

        initInterstitialAd()

        return binding.root
    }

    private fun initStatisticEmojis(){
        binding.gameScoreEmoji.setText("\uD83C\uDFAF")
        binding.mistakeEmoji.setText("\uD83D\uDCDB")
    }

    private fun initInterstitialAd() {
        interstitialAd.adUnitId = INTERSTITIAL_VIDEO_ID
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    private fun loadUserScore() {
        viewModelAccount = injectViewModel(viewModelFactoryAccount)
        viewModelAccount.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        score = it.data!!.score
                    }
                }
            }
        })
    }

    private fun initGameKeyBoardAdapter() {
        gameKeyboardAdapter =
            GameKeyBoardRecyclerViewAdapter(
                GameKeyBoardRecyclerViewAdapter.OnEmojiClickListener {
                    winOrder(it!!.unicode)
                })
        binding.scrollFinderField.adapter = gameKeyboardAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                onBackButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }

    private fun initButtons() {
        binding.gameBackButton.setOnClickListener {
            if (this.view != null) {
                onBackButton()
            }
        }
    }

    private fun startGameDialog() {
        if (!gameHint.isHintShown()) {
            CampaignGameHint.show(level)
            CampaignGameHint.getStartGameButton().setOnClickListener {
                CampaignGameHint.dialogView.dismiss()
            }
            gameHint.isHintShown(true)
        }

        setEmptyStatistic()
        initProgressAnimator(level)
        addEndAnimationListener()
        initStartCircleEndAnimation()
    }

    private fun initStartCircleEndAnimation() {
        startTimer()
    }

    private fun onBackButton() {
        animation.pause()

        ExitGameDialog.create(this)
        ExitGameDialog.open()
        ExitGameDialog.getGameExitButton().setOnClickListener {
            ExitGameDialog.dialogView.dismiss()
            animation.removeAllListeners()
            animation.cancel()
            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
            this@GameFragment.findNavController().navigateUp()
        }

        ExitGameDialog.getResumeGameButton().setOnClickListener {
            animation.resume()
            ExitGameDialog.dialogView.dismiss()
        }
    }

    private fun setMusicSwitcher() {
        binding.gameAudioBtn.setOnClickListener {
            if(settingsPrefs.isPlayMusic()){
                (requireActivity() as MainActivity).mediaPlayerPool.pauseBackground()
                settingsPrefs.changeMusic(!settingsPrefs.isPlayMusic())
                initAudioButton()
            } else {
                settingsPrefs.changeMusic(!settingsPrefs.isPlayMusic())
                (requireActivity() as MainActivity).mediaPlayerPool.createPlayers()
                (requireActivity() as MainActivity).mediaPlayerPool.playBackground()
                initAudioButton()
            }
        }
    }

    private fun initAudioButton() {
        if(settingsPrefs.isPlayMusic()){
            binding.gameAudioBtn.icon = resources.getDrawable(R.drawable.icons8_audio_24px)
        } else {
            binding.gameAudioBtn.icon = resources.getDrawable(R.drawable.icons8_no_audio_24px)
        }
    }

    private fun createKeyboardLevel(data: List<EmojiShopModel?>) {
        val uniqueList = data.distinctBy {
            it?.unicode
        }
        gameKeyboardAdapter.submitList(uniqueList + getRandomExtraEmojis(uniqueList.size))
        binding.keyboardProgressBar.visibility = View.GONE
    }

    private fun getRandomExtraEmojis(size: Int): List<EmojiShopModel> {
        return if (size < MAX_KEYBOARD_EMOJIS) {
            Emoji.emojiList.drop(size).toEmojiShopModel()
        } else {
            emptyList()
        }
    }

    private fun winOrder(emoji: String) {
        if (emoji == list[number]?.unicode) {
            binding.gameScore.text = (binding.gameScore.text.toString().toInt() + 10).toString()
            levelEditTextList[number].alpha = 0.4f
            if (++number != list.size) {
                levelEditTextList[number].alpha = 1.0f
            } else {
                if (animation.isRunning) {
                    animation.removeAllListeners()
                    animation.pause()

                    val statistics = getLevelStatistics(resources.getString(R.string.win_game))

                    EndGameDialog.showEndGameDialog(this, statistics, getLevelsState())
                    createEndGameListeners()

                    viewModel.writeGameStatistic(level.title, statistics)
                    viewModel.updateScore(score + statistics.score)
                    viewModel.updateEmos(statistics.score / 5)

                    (activity as MainActivity).mediaPlayerPool.play(MusicType.WIN)

                } else {
                    setEmptyStatistic()
                }
            }
            playSound(MusicType.CORRECT)
        } else {
            playSound(MusicType.FAIL)
            binding.gameMistakes.text =
                (binding.gameMistakes.text.toString().toInt() + 1).toString()
        }
    }

    private fun playSound(type: MusicType) {
        (activity as MainActivity).mediaPlayerPool.play(type)
    }

    private fun stopSound() {
        (activity as MainActivity).mediaPlayerPool.stop(MusicType.GAME)
    }

    private fun getLevelsState(): State {
        return if (levelId == levels.size - 1) {
            State.FINISHED
        } else {
            State.LOST
        }
    }

    private fun createEndGameListeners() {
        EndGameDialog.getRetryButton().setOnClickListener {
            EndGameDialog.dialogView.dismiss()
            interstitialAd.loadAd(AdRequest.Builder().build())
            startGameDialog()
        }

        EndGameDialog.getNextLevelButton().setOnClickListener {
            startNextLevel()
            interstitialAd.loadAd(AdRequest.Builder().build())
            EndGameDialog.dialogView.dismiss()
        }
    }

    private fun getLevelStatistics(status: String): UserLevelStatistics {
        return UserLevelStatistics(
            score = binding.gameScore.text.toString().toInt(),
            mistakes = binding.gameMistakes.text.toString().toInt(),
            time = (animation.currentPlayTime.toDouble() / 1000.0).toString(),
            id = levelId,
            max_score = (10 * list.size),
            title = level.title,
            result = status
        )
    }

    private fun getGameCategory() {
        level = GameFragmentArgs.fromBundle(
            requireArguments()
        ).Category

        levels = GameFragmentArgs.fromBundle(
            requireArguments()
        ).Levels.toList()

        levelId = level.id
        binding.level = level
    }

    private fun initProgressAnimator(levelModel: SmallLevelModel) {
        binding.gameProgressBar.max = levelModel.time * 100
        binding.gameProgressBar.progress = levelModel.time * 100
        animation =
            ObjectAnimator.ofInt(binding.gameProgressBar, "progress", levelModel.time * 100, 0)
        animation.duration = (levelModel.time * 1000).toLong()
        animation.interpolator = AccelerateInterpolator()
    }

    private fun addEndAnimationListener() {
        animation.addListener(onEnd = {
            val statistics = getLevelStatistics(resources.getString(R.string.game_lost))
            showEndGameDialog(statistics)
            playSound(MusicType.LOSE)
            showInertialVideoAd()

            if (number != 0) {
                viewModel.writeGameStatistic(level.title, statistics)
                viewModel.updateScore(score + statistics.score)
            }

            stopSound()
        })

        animation.addUpdateListener {
            binding.gameTime.text = (it.animatedValue.toString().toInt() / 100).toString()
        }
    }

    private fun showInertialVideoAd() {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        }
    }

    private fun showEndGameDialog(statistics: UserLevelStatistics) {
        EndGameDialog.showEndGameDialog(this, statistics, getLevelsState())
        createEndGameListeners()
    }

    private fun startNextLevel() {
        levelId++
        if (levelId < levels.size) {
            for (level in levels) {
                if (level.id == levelId) {
                    this.level = level
                    initProgressAnimator(level)
                    startGameDialog()
                    levelViewModel.fetchLevel(level.title)
                }
            }
        }
    }

    private fun loadGameLevel() {
        levelViewModel.fetchLevel(level.title)
        levelViewModel.levelResponse.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.levelProgressBar.visibility = View.VISIBLE
                        binding.keyboardProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        drawLevel(result.data)
                        createKeyboardLevel(result.data)
                    }
                    is Result.Error -> {
                        //ShowStartGameButton.error(result.exception.message)
                    }
                }
            }
        })
    }

    private fun drawLevel(data: List<EmojiShopModel?>) {

        binding.gameEmojiField.removeAllViews()
        levelEditTextList = mutableListOf()

        list = data.sortedBy { e -> e?.order }
        randomList.addAll(list)
        val emojiSize = ScreenSize.getScreenSize(resources, list)

        for (emoji in list) {
            if (emoji != null) {
                val emojiView = EmojiAppCompatEditText(requireContext())

                emojiView.isFocusable = false
                emojiView.isSingleLine = true
                emojiView.maxEmojiCount = 1

                if (emoji.order == 0) {
                    emojiView.alpha = 1.0f
                } else {
                    emojiView.alpha = 0.4f
                }
                emojiView.background = null

                emojiView.textSize = emojiSize
                emojiView.setText(EmojiCompat.get().process(emoji.unicode))

                val params: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(
                        GridLayout.spec(emoji.x, 1f),
                        GridLayout.spec(emoji.y, 1f)
                    )

                params.setMargins(0, 0, 0, 0)
                emojiView.setPadding(0, 0, 0, 0)
                emojiView.layoutParams = params

                levelEditTextList.add(emojiView)
                binding.gameEmojiField.addView(emojiView)
            }
        }
        binding.levelProgressBar.visibility = View.GONE
    }

    private fun setEmptyStatistic() {
        binding.gameScore.text = "0"
        binding.gameMistakes.text = "0"
        binding.gameTime.text = level.time.toString()
        if (!levelEditTextList.isNullOrEmpty()) {

            if (number >= levelEditTextList.size) {
                levelEditTextList[number - 1].alpha = 0.4f
            } else {
                levelEditTextList[number].alpha = 0.4f
            }

            number = 0
            keyboardNumber = 0
            levelEditTextList[number].alpha = 1.0f
        }
    }

    private fun startTimer() {
        animation.start()
        Log.d("animation", "start")
    }

}