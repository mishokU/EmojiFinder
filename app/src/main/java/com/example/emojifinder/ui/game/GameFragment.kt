package com.example.emojifinder.ui.game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
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
import com.example.emojifinder.ui.utils.GameDialogs
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

    lateinit var binding : FragmentGameBinding
    private lateinit var animation: ObjectAnimator

    private lateinit var level : SmallLevelModel

    private var list : List<EmojiModel?> = listOf()
    private var levelEditTextList : MutableList<EmojiAppCompatEditText> = mutableListOf()

    private var number : Int = 0
    private var keyboardNumber : Int = 0

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

        initProgressAnimator()
        backToLevels()
        startGameDialog()
        loadGameLevel()

        return binding.root
    }


    private fun createTestLevel(){
        for(i : Int in 1..10){
            for(j : Int in 1..10){
                val emojiView = EmojiAppCompatEditText(requireContext())
                emojiView.textSize = ScreenSize.getScreenSize(resources)

                emojiView.isFocusable = false
                emojiView.isSingleLine = true
                emojiView.maxEmojiCount = 1
                //emojiView.isEnabled = false
                emojiView.background = null


                emojiView.setPadding(0,0,0,0)

                emojiView.setText(Emoji.getEmojiByUnicode(0x1F62F))
                emojiView.alpha = 1.0f
                binding.gameEmojiField.addView(emojiView)
            }
        }
    }


    private fun createKeyboardLevel() {
        for(i : Int in 1..4){
            val horizontal = LinearLayout(requireContext())
            horizontal.gravity = Gravity.CENTER_HORIZONTAL

            for(j : Int in 1..4){

                val emojiView = EmojiAppCompatButton(requireContext())

                emojiView.isFocusable = false
                emojiView.isSingleLine = true
                emojiView.textSize = ScreenSize.getScreenSize(resources)
                emojiView.background = resources.getDrawable(R.drawable.ripple_color)
                emojiView.setOnClickListener {
                    winOrder(emojiView.text.toString())
                }

                if(keyboardNumber != list.size) {
                    emojiView.text = list[keyboardNumber]?.unicode
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

                animation.removeAllListeners()
                animation.pause()

                val statistics = getLevelStatistics("Win")
                GameDialogs.showEndGameDialog(this, statistics)
                createEndGameListeners()

                viewModel.writeGameStatistic(level.title, statistics)
            }
        } else {
            binding.gameMistakes.text = (binding.gameMistakes.text.toString().toInt() + 1).toString()
        }
    }

    private fun createEndGameListeners() {
        GameDialogs.getRetryButton().setOnClickListener{
            GameDialogs.alert.dismiss()
            startGameDialog()
        }

        GameDialogs.getNextLevelButton().setOnClickListener {
            startNextLevel()
        }

        GameDialogs.getUpperRetryButton().setOnClickListener {
            GameDialogs.alert.dismiss()
            startGameDialog()
        }

        GameDialogs.getUpperBackButton().setOnClickListener {
            GameDialogs.alert.dismiss()
            this.findNavController().popBackStack()
        }
    }

    private fun getLevelStatistics(status : String): UserLevelStatistics {
        return UserLevelStatistics(
            score = binding.gameScore.text.toString().toInt(),
            mistakes = binding.gameMistakes.text.toString().toInt(),
            time = binding.gameTime.text.toString(),
            id = binding.gameLevel.text.toString().toInt(),
            result = status
        )
    }

    private fun getGameCategory() {
        level = GameFragmentArgs.fromBundle(
            requireArguments()
        ).Category
    }

    private fun initProgressAnimator() {
        animation = ObjectAnimator
            .ofInt(binding.gameProgressBar, "progress", 100, 0)
        animation.duration = 5000
        animation.interpolator = AccelerateInterpolator()

        animation.addListener(onEnd = {
            val statistics = getLevelStatistics("Lost")
            showEndGameDialog(statistics)
            viewModel.writeGameStatistic(level.title, statistics)
        })
    }

    private fun showEndGameDialog(statistics: UserLevelStatistics) {
        GameDialogs.showEndGameDialog(this, statistics)
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
                        drawLevel(result.data)
                        createKeyboardLevel()
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun drawLevel(data: List<EmojiModel?>) {
        list = data.sortedBy { e -> e?.order }
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

                emojiView.textSize = ScreenSize.getScreenSize(resources)
                emojiView.setText(EmojiCompat.get().process(emoji.unicode))

                val params: GridLayout.LayoutParams =
                    GridLayout.LayoutParams(
                        GridLayout.spec(emoji.x, 0f),
                        GridLayout.spec(emoji.y, 0f)
                    )

                params.setMargins(0,0,0,0)
                emojiView.setPadding(0,0,0,0)
                emojiView.layoutParams = params

                levelEditTextList.add(emojiView)
                binding.gameEmojiField.addView(emojiView)
            }
        }
    }

    private fun backToLevels() {
        requireActivity().onBackPressedDispatcher.addCallback(this){
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
        GameDialogs.showStartDialog(this)
        GameDialogs.alert.setOnDismissListener {
            startTimer()
        }
        GameDialogs.getStartGameButton().setOnClickListener {
            GameDialogs.alert.dismiss()
        }
    }

    private fun setEmptyStatistic() {
        binding.gameScore.text = "0"
        binding.gameMistakes.text = "0"
        binding.gameTime.text = level.time.toString()
        number = 0
        keyboardNumber = 0
    }

    private fun startTimer() {
        animation.start()
    }

}
