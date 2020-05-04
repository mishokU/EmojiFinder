package com.example.emojifinder.ui.game

import android.animation.ObjectAnimator
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.FragmentGameBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.GameViewModel
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.utils.GameDialogs
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
    private lateinit var gameDialog : GameDialogs

    private lateinit var category : SmallLevelModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        levelViewModel = injectViewModel(levelViewModelFactory)

        gameDialog = GameDialogs(this)

        getGameCategory()
        initProgressAnimator()

        backToLevels()
        startGameDialog()

        loadGameLevel()

        return binding.root
    }

    private fun getGameCategory() {
        category = GameFragmentArgs.fromBundle(
            requireArguments()
        ).Category
    }

    private fun initProgressAnimator() {
        animation = ObjectAnimator
            .ofInt(binding.gameProgressBar, "progress", 100, 0)
        animation.duration = 3000
        animation.interpolator = AccelerateInterpolator()
        animation.addListener(onEnd = {
            val statistics = UserLevelStatistics(
                score = binding.gameScore.text.toString().toInt(),
                mistakes = binding.gameMistakes.text.toString().toInt(),
                time = binding.gameTime.text.toString(),
                id = binding.gameLevel.text.toString().toInt(),
                result = "Lost"
            )
            showEndGameDialog(statistics)
            viewModel.writeGameStatistic(category.title, statistics)
        })
    }

    private fun showEndGameDialog(statistics: UserLevelStatistics) {
        gameDialog.showEndGameDialog(statistics)

        gameDialog.getRetryButton().setOnClickListener{
            gameDialog.alert.dismiss()
            startGameDialog()
        }

        gameDialog.getNextLevelButton().setOnClickListener {
            startNextLevel()
        }

    }

    private fun startNextLevel() {

    }

    private fun loadGameLevel(){
        levelViewModel.fetchLevel(category.title)
        levelViewModel.levelResponse.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when(result){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        print(result.data)
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun backToLevels() {
        requireActivity().onBackPressedDispatcher.addCallback(this){
            animation.pause()
            gameDialog.showExitDialog()

            gameDialog.getGameExitButton().setOnClickListener {
                animation.cancel()
                gameDialog.alert.dismiss()
                this@GameFragment.findNavController().popBackStack()
            }
            gameDialog.getResumeGameButton().setOnClickListener {
                animation.resume()
                gameDialog.alert.dismiss()
            }
        }
    }

    private fun showSoftKeyboard() {
        binding.keyboardTrigger.isFocusableInTouchMode = true
        binding.keyboardTrigger.requestFocus()

        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(binding.keyboardTrigger, InputMethodManager.SHOW_FORCED)
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        showSoftKeyboard()

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
        gameDialog.showStartDialog()
        gameDialog.alert.setOnDismissListener {
            startTimer()
        }
        gameDialog.getStartGameButton().setOnClickListener {
            gameDialog.alert.dismiss()
        }
    }

    private fun startTimer() {
        animation.start()
    }

}
