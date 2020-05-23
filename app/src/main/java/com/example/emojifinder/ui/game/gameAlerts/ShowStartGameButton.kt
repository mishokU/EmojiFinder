package com.example.emojifinder.ui.game.gameAlerts

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.text.BoringLayout
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.emojifinder.R
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment
import java.util.*

object ShowStartGameButton {

    lateinit var dialogView : Dialog
    private lateinit var fragment: DaggerFragment
    private lateinit var count : LottieAnimationView
    private lateinit var start : MaterialButton
    private lateinit var loadingPlace : RelativeLayout

    private var firstPlay : Boolean = true

    fun create(
        fragment: DaggerFragment,
        level: SmallLevelModel){

        this.fragment = fragment

        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        // layout to display
        dialogView.setContentView(R.layout.game_start_alert_dialog)

        // set color transparent
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));

        setGameTitle(level)
        initStartButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setGameTitle(level: SmallLevelModel) {
        val title : TextView = dialogView.findViewById(R.id.level_title)
        val first : String = level.title?.get(0).toString().toUpperCase(Locale.ROOT)
        val all : String? = level.title?.drop(1)
        title.text = first + all
    }

    fun show(){
        dialogView.show()
        if(!firstPlay) {
            count.playAnimation()
        }
    }

    private fun initStartButton(){
        start = dialogView.findViewById(R.id.start_circle_game_button)

        fragment.bindProgressButton(start)
        start.showProgress {
            progressColor = ContextCompat.getColor(fragment.requireContext(),R.color.background_color)
            buttonText = ""
        }

        initAnimation()
        setOnClickListener()
    }

    private fun initAnimation() {
        loadingPlace = dialogView.findViewById(R.id.loading_game_place)
        loadingPlace.animate().alpha(0.0f).setDuration(3000).setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    count.playAnimation()
                }
            }
        ).start()
    }

    private fun setOnClickListener() {
        count = dialogView.findViewById(R.id.count_down_timer)

        start.setOnClickListener {

            firstPlay = false
            if(firstPlay) {
                initAnimation()
            }
        }
    }

    fun countListener() = count

    fun loaded() {
        start.hideProgress("Loaded")
    }


}