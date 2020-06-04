package com.example.emojifinder.ui.game

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.FragmentGameBinding
import com.example.emojifinder.domain.prefs.ShowGameHintPrefs
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MusicType
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.GameViewModel
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.game.gameAlerts.EndGameDialog
import com.example.emojifinder.ui.game.gameAlerts.GameDialogs
import com.example.emojifinder.ui.game.gameAlerts.ExitGameDialog
import com.example.emojifinder.ui.game.gameAlerts.ShowStartGameButton
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.utils.ScaleGesture
import com.example.emojifinder.ui.utils.ScreenSize
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class GameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : GameViewModel

    @Inject
    lateinit var levelViewModelFactory: ViewModelProvider.Factory
    lateinit var levelViewModel : CategoriesViewModel

    @Inject
    lateinit var gameHint : ShowGameHintPrefs

    lateinit var binding : FragmentGameBinding
    private lateinit var animation: ObjectAnimator
    private lateinit var gameKeyboardAdapter : GameKeyBoardRecyclerViewAdapter

    private lateinit var level : SmallLevelModel

    private var list : List<EmojiShopModel?> = listOf()
    private var levelEditTextList : MutableList<EmojiAppCompatEditText> = mutableListOf()
    private lateinit var ShowStartGameButton : ShowStartGameButton

    private var number : Int = 0
    private var keyboardNumber : Int = 0

    private val randomList : MutableList<EmojiShopModel?> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        levelViewModel = injectViewModel(levelViewModelFactory)

        binding.gameEmojiField.setOnTouchListener(ScaleGesture(requireContext()))

        ShowStartGameButton = ShowStartGameButton()

        getGameCategory()

        initButtons()

        initDialogs()

        initGameKeyBoardAdapter()

        loadGameLevel()

        startGameDialog()

        return binding.root
    }

    private fun initDialogs() {
        ShowStartGameButton.create(this, level)
    }

    private fun initGameKeyBoardAdapter() {
        gameKeyboardAdapter = GameKeyBoardRecyclerViewAdapter(
            GameKeyBoardRecyclerViewAdapter.OnEmojiClickListener{
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
            if(this.view != null){
                onBackButton()
            }
        }
    }

    private fun startGameDialog() {
        if(!gameHint.isHintShown()){
            GameDialogs.showStartDialog(this, level)
            GameDialogs.alert.setOnDismissListener {
                GameDialogs.alert.dismiss()
                ShowStartGameButton.show()
            }
            GameDialogs.getStartGameButton().setOnClickListener {
                GameDialogs.alert.dismiss()
                ShowStartGameButton.show()
            }
            gameHint.isHintShown(true)
        } else {
            ShowStartGameButton.show()
        }

        setEmptyStatistic()
        initProgressAnimator()
        addEndAnimationListener()
        initStartCircleEndAnimation()
    }

    private fun initStartCircleEndAnimation(){
        ShowStartGameButton.countListener().addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationEnd(animation: Animator?) {
                ShowStartGameButton.dialogView.dismiss()
                startTimer()
            }
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
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
            this@GameFragment.findNavController().popBackStack()
        }

        ExitGameDialog.getResumeGameButton().setOnClickListener {
            animation.resume()
            ExitGameDialog.dialogView.dismiss()
            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
        }
    }

    private fun createKeyboardLevel(data: List<EmojiShopModel?>) {
       gameKeyboardAdapter.submitList(data)
    }

    private fun winOrder(emoji: String) {
        if(emoji == list[number]?.unicode){
            binding.gameScore.text = (binding.gameScore.text.toString().toInt() + 20).toString()
            levelEditTextList[number].alpha = 0.4f
            if(++number != list.size) {
                levelEditTextList[number].alpha = 1.0f
            } else {
                if(animation.isRunning){
                    animation.removeAllListeners()
                    animation.pause()

                    val statistics = getLevelStatistics(resources.getString(R.string.win_game))
                    EndGameDialog.showEndGameDialog(this, statistics)
                    createEndGameListeners()

                    viewModel.writeGameStatistic(level.title, statistics)
                    viewModel.updateEmos(statistics.score / 5)

                    (activity as MainActivity).mediaPlayerPool.play(MusicType.WIN)

                } else {
                    setEmptyStatistic()
                }
            }
        } else {
            binding.gameMistakes.text = (binding.gameMistakes.text.toString().toInt() + 1).toString()
        }
    }

    private fun createEndGameListeners() {
        EndGameDialog.getRetryButton().setOnClickListener{
            EndGameDialog.dialogView.dismiss()
            startGameDialog()
        }

        EndGameDialog.getNextLevelButton().setOnClickListener {
            if(EndGameDialog.getNextLevelButton().text == resources.getString(R.string.exit)){
                EndGameDialog.dialogView.dismiss()
                this.findNavController().popBackStack()
            } else {
                startNextLevel()
            }
        }

        EndGameDialog.getUpperRetryButton().setOnClickListener {
            EndGameDialog.dialogView.dismiss()
            startGameDialog()
        }

        EndGameDialog.getUpperBackButton().setOnClickListener {
            EndGameDialog.dialogView.dismiss()
            this.findNavController().popBackStack()
        }
    }

    private fun getLevelStatistics(status : String): UserLevelStatistics {
        return UserLevelStatistics(
            score = binding.gameScore.text.toString().toInt(),
            mistakes = binding.gameMistakes.text.toString().toInt(),
            time = (animation.currentPlayTime.toDouble() / 1000.0).toString(),
            id = binding.gameLevel.text.toString().toInt(),
            max_score = (20 * list.size),
            title = level.title!!,
            result = status
        )
    }

    private fun getGameCategory() {
        level = GameFragmentArgs.fromBundle(
            requireArguments()
        ).Category
        binding.gameLevel.text = level.id.toString()
        binding.level = level
    }

    private fun initProgressAnimator() {
        animation = ObjectAnimator
            .ofInt(binding.gameProgressBar, "progress", 100, 0)
        animation.duration = (level.time * 1000).toLong()
        animation.interpolator = AccelerateInterpolator()
    }

    private fun addEndAnimationListener(){
        animation.addListener(onEnd = {
            val statistics = getLevelStatistics(resources.getString(R.string.game_lost))
            showEndGameDialog(statistics)
            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
        })
        animation.addUpdateListener {
            binding.gameTime.text = it.animatedValue.toString()
        }
    }

    private fun showEndGameDialog(statistics: UserLevelStatistics) {
        EndGameDialog.showEndGameDialog(this, statistics)
        createEndGameListeners()
    }

    private fun startNextLevel() {

    }

    private fun loadGameLevel(){
        levelViewModel.fetchLevel(level.title)
        levelViewModel.levelResponse.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when(result){
                    is Result.Loading -> {
                        binding.levelProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.levelProgressBar.visibility = View.GONE

                        hidePlaceholder()

                        drawLevel(result.data)
                        createKeyboardLevel(result.data)
                    }
                    is Result.Error -> {
                        ShowStartGameButton.error(result.exception.message)
                    }
                }
            }
        })
    }

    private fun hidePlaceholder() {
        ShowStartGameButton.loaded()
    }

    private fun drawLevel(data: List<EmojiShopModel?>) {

        list = data.sortedBy { e -> e?.order }
        randomList.addAll(list)
        val emojiSize = ScreenSize.getScreenSize(resources, list)

        for(emoji in list){
            if(emoji != null){
                val emojiView = EmojiAppCompatEditText(requireContext())
                emojiView.isFocusable = false
                emojiView.isSingleLine = true
                emojiView.maxEmojiCount = 1

                if(emoji.order == 0){
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

                params.setMargins(0,0,0,0)
                emojiView.setPadding(0,0,0,0)
                emojiView.layoutParams = params

                levelEditTextList.add(emojiView)
                binding.gameEmojiField.addView(emojiView)
            }
        }
    }

    private fun setEmptyStatistic() {
        binding.gameScore.text = "0"
        binding.gameMistakes.text = "0"
        binding.gameTime.text = level.time.toString()
        if(!levelEditTextList.isNullOrEmpty()) {

            if(number >= levelEditTextList.size){
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
    }

}
