package com.example.emojifinder.ui.game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.NonNull
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import androidx.core.content.ContextCompat
import androidx.emoji.text.EmojiCompat
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.EmojiModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.FragmentGameBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.GameViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.utils.EndGameDialog
import com.example.emojifinder.ui.utils.GameDialogs
import com.example.emojifinder.ui.utils.ScaleGesture
import com.example.emojifinder.ui.utils.ScreenSize
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class GameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : GameViewModel

    @Inject
    lateinit var levelViewModelFactory: ViewModelProvider.Factory
    lateinit var levelViewModel : CategoriesViewModel

    lateinit var binding : FragmentGameBinding
    private lateinit var animation: ObjectAnimator

    private lateinit var level : SmallLevelModel

    private var list : List<EmojiModel?> = listOf()
    private var levelEditTextList : MutableList<EmojiAppCompatEditText> = mutableListOf()

    private var number : Int = 0
    private var keyboardNumber : Int = 0

    private val randomList : MutableList<EmojiModel?> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        levelViewModel = injectViewModel(levelViewModelFactory)

        binding.gameEmojiField.setOnTouchListener(ScaleGesture(requireContext()))


        getGameCategory()

        initButtons()
        startGameDialog()
        loadGameLevel()



        return binding.root
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

    private fun onBackButton() {
        animation.pause()
        GameDialogs.showExitDialog(this@GameFragment)

        GameDialogs.getGameExitButton().setOnClickListener {
            GameDialogs.alert.dismiss()
            animation.removeAllListeners()
            animation.cancel()
            this@GameFragment.findNavController().popBackStack()
        }

        GameDialogs.getResumeGameButton().setOnClickListener {
            animation.resume()
            GameDialogs.alert.dismiss()
        }

        GameDialogs.alert.setOnDismissListener {
            animation.resume()
        }
    }

    private fun createKeyboardLevel() {
        val emojiSize = ScreenSize.getEmojiSize(resources)
        for(i : Int in 1..4){
            val horizontal = LinearLayout(requireContext())
            horizontal.gravity = Gravity.CENTER_HORIZONTAL
            for(j : Int in 0..10){

                val emojiView = EmojiAppCompatButton(requireContext())

                emojiView.isFocusable = false
                emojiView.isSingleLine = true
                emojiView.textSize = emojiSize
                emojiView.background = resources.getDrawable(R.drawable.ripple_color)
                emojiView.setOnClickListener {
                    winOrder(emojiView.text.toString())
                }

                if(keyboardNumber != list.size) {
                    val element = randomList.random()
                    emojiView.text = element?.unicode
                    randomList.remove(element)
                    keyboardNumber++
                } else {
                    emojiView.text = Emoji.getRandomEmoji()
                }
                horizontal.addView(emojiView)
            }
            binding.finderGameField.addView(horizontal)
        }
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

                    val statistics = getLevelStatistics("Win")
                    EndGameDialog.showEndGameDialog(this, statistics)
                    createEndGameListeners()

                    viewModel.writeGameStatistic(level.title, statistics)
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
            startNextLevel()
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
    }

    private fun initProgressAnimator() {
        animation = ObjectAnimator
            .ofInt(binding.gameProgressBar, "progress", 100, 0)
        animation.duration = (level.time * 1000).toLong()
        animation.interpolator = AccelerateInterpolator()
    }

    private fun addEndAnimationListener(){
        animation.addListener(onEnd = {
            val statistics = getLevelStatistics("Lost")
            showEndGameDialog(statistics)
            viewModel.writeGameStatistic(level.title, statistics)
        })
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
                        GameDialogs.getStartGameButton().showProgress {
                            this.progressColor = ContextCompat.getColor(requireContext(),R.color.main_color)
                            this.buttonText = ""
                        }
                    }
                    is Result.Success -> {
                        binding.levelProgressBar.visibility = View.GONE

                        //viewModel.drawLevel(this, result.data)
                        //viewModel.drawKeyBoard(this, result.data)
                        drawLevel(result.data)
                        createKeyboardLevel()
                        GameDialogs.getStartGameButton().hideProgress(resources.getString(R.string.start_the_game))

                        addEndAnimationListener()
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun drawLevel(data: List<EmojiModel?>) {

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

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        animation.addPauseListener(onResume = {
            it.resume()
        })
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        animation.addPauseListener(onPause = {
            it.pause()
        })
    }

    private fun startGameDialog() {
        setEmptyStatistic()
        initProgressAnimator()
        GameDialogs.showStartDialog(this, level.time)
        GameDialogs.alert.setOnDismissListener {
            startTimer()
        }
        bindProgressButton(GameDialogs.getStartGameButton())
        GameDialogs.getStartGameButton().setOnClickListener {
            GameDialogs.alert.dismiss()
        }
    }

    private fun setEmptyStatistic() {
        binding.gameScore.text = "0"
        binding.gameMistakes.text = "0"
        binding.gameTime.text = level.time.toString()
        if(!levelEditTextList.isNullOrEmpty()) {
            if(number != 0){
                levelEditTextList[number-1].alpha = 0.4f
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
