package com.example.emojifinder.ui.game

import android.animation.ObjectAnimator
import android.content.Context
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
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.databinding.FragmentGameBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.GameViewModel
import com.example.emojifinder.ui.categories.CategoryModel
import com.example.emojifinder.ui.utils.GameDialogs
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class GameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : GameViewModel

    lateinit var binding : FragmentGameBinding
    private lateinit var animation: ObjectAnimator
    private lateinit var gameDialog : GameDialogs

    private lateinit var category : CategoryModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)

        gameDialog = GameDialogs()

        getGameCategory()
        initProgressAnimator()
        openKeyboard()
        backToLevels()
        //startDialog()

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
            viewModel.writeGameStatistic(category.title, statistics)
        })
    }

    private fun observeGameStatistic(){
        viewModel.statisticResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        gameDialog.showEndGameDialog(this@GameFragment, it.data)
                        gameDialog.getRetryButton().setOnClickListener{

                        }
                    }
                    is Result.Error -> {

                    }
                }
                viewModel.statisticResponseComplete()
            }
        })
    }

    private fun backToLevels() {
        requireActivity().onBackPressedDispatcher.addCallback(this){
            animation.pause()
            gameDialog.showExitDialog(this@GameFragment)
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



    private fun openKeyboard() {
        binding.triggerKeyboard.requestFocus()
        val imgr = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
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


    private fun startDialog() {
        val dialog = GameDialogs().showStartDialog(this)
        val button = dialog.dialogView.findViewById<View>(R.id.start_game_btn)
        dialog.alert.setOnDismissListener {
            startTimer()
        }
        button.setOnClickListener {
            dialog.alert.dismiss()
        }
    }

    private fun startTimer() {
        animation.start()
    }

}
