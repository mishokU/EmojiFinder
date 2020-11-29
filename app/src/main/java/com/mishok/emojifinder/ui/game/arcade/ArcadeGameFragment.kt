package com.mishok.emojifinder.ui.game.arcade

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.ArcadeGameLayoutBinding
import com.mishok.emojifinder.domain.adds.INTERSTITIAL_ID
import com.mishok.emojifinder.domain.prefs.SettingsPrefs
import com.mishok.emojifinder.domain.prefs.ShowGameHintPrefs
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.sounds.MusicType
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import com.mishok.emojifinder.domain.viewModels.GameViewModel
import com.mishok.emojifinder.ui.game.campaign.gameAlerts.ExitGameDialog
import com.mishok.emojifinder.ui.main.MainActivity
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.arcade_game_layout.*
import javax.inject.Inject

class ArcadeGameFragment : DaggerFragment() {

    private lateinit var binding : ArcadeGameLayoutBinding
    private lateinit var userEmojisAdapter : GameRecyclerViewAdapter
    private lateinit var animation: ObjectAnimator
    private lateinit var interstitialAd : InterstitialAd
    private lateinit var allEmojis : MutableList<EmojiShopModel?>
    private lateinit var oneLevelEmojis : MutableList<EmojiShopModel?>

    var emojis : MutableList<EmojiAppCompatEditText> = mutableListOf()

    private var findEmojis = 0
    var score = 0
    private var userScore = 0
    private var userEmos = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GameViewModel

    @Inject
    lateinit var viewModelFactoryAccount: ViewModelProvider.Factory
    private lateinit var viewModelAccount : AccountViewModel

    @Inject
    lateinit var settingsPrefs: SettingsPrefs

    @Inject
    lateinit var showGameHintPrefs: ShowGameHintPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArcadeGameLayoutBinding.inflate(inflater)

        interstitialAd = InterstitialAd(requireContext())

        viewModelAccount = injectViewModel(viewModelFactoryAccount)
        viewModel = injectViewModel(viewModelFactory)

        EndGameDialog.create(this)

        initAudioButton()
        setMusicSwitcher()

        initEndGameButtons()

        initButtons()

        showGameHint()
        initFinderEmojis()
        initAdapter()
        initViewModel()

        loadUserScore()

        initloadAd()

        return binding.root
    }

    private fun showGameHint() {
        if(!showGameHintPrefs.isArcadeHintShown()){
            ArcadeGameHint.create(this)
            ArcadeGameHint.show()
            ArcadeGameHint.getStartGameButton().setOnClickListener {
                initTimer()
                animation.start()
                showGameHintPrefs.isArcadeHintShown(true)
                ArcadeGameHint.dialogView.dismiss()
            }
        } else {
            initTimer()
        }
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

    private fun loadUserScore() {
        viewModelAccount.fetchMainUserData()
        viewModelAccount.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        userScore = it.data!!.score
                    }
                }
            }
        })

        viewModelAccount.fetchUserValues()
        viewModelAccount.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        userEmos = it.data.emos
                    }
                }
            }
        })

        viewModel.oneLevelEmojisData.observe(viewLifecycleOwner, Observer {
            it?.let {
                //userEmojisAdapter.submitList(it)
            }
        })

        viewModel.bottomEmojis.observe(viewLifecycleOwner, Observer {
            it?.let {
                createBottomEmojis(it)
            }
        })
    }

    private fun onBackButton() {
        animation.pause()

        ExitGameDialog.create(this)
        ExitGameDialog.open()

        ExitGameDialog.getGameExitButton().setOnClickListener {
            animation.removeAllListeners()
            Handler().postDelayed({
                ExitGameDialog.dialogView.dismiss()
            }, 100)
            animation.cancel()
            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
            this.findNavController().popBackStack()
        }

        ExitGameDialog.getResumeGameButton().setOnClickListener {
            animation.resume()
            ExitGameDialog.dialogView.dismiss()
        }
    }

    private fun initEndGameButtons() {
        EndGameDialog.getRetryButton().setOnClickListener {
            createGamePlace(allEmojis)
            findEmojis = 0
            initTimer()
            score = 0
            animation.start()

            interstitialAd.loadAd(AdRequest.Builder().build())

            EndGameDialog.dialogView.dismiss()
        }

        EndGameDialog.getExitButton().setOnClickListener {
            Handler().postDelayed({
                EndGameDialog.dialogView.dismiss()
            }, 100)
            this.findNavController().popBackStack()
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun initTimer() {
        binding.gameProgressBar.max = 60 * 100
        animation = ObjectAnimator.ofInt(binding.gameProgressBar,"progress", 60*100, 0)
        animation.duration = (60 * 1000).toLong()
        animation.interpolator = LinearInterpolator()

        animation.addUpdateListener {
            binding.gameTime.text = (it.animatedValue.toString().toInt() / 100).toString()
        }

        animation.addListener(onEnd = {
            playSound(MusicType.WIN)
            stopSound(MusicType.GAME)
            loadAd()
            if(score == 0){
                EndGameDialog.open(score)
            } else {
                EndGameDialog.open(score)
                viewModelAccount.updateScore(score + userScore)
                viewModelAccount.updateUserEmos(userEmos + score / 5)
            }
        })

        animation.addUpdateListener {
            binding.gameTime.text = (it.animatedValue.toString().toInt() / 100).toString()
            if(it.animatedValue.toString().toInt() / 100 == 60 / 2){
                binding.gameTime.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.textOrangeColor))
            }
        }
    }

    private fun loadAd() {
        if(interstitialAd.isLoaded){
            interstitialAd.show()
        }
    }

    private fun initloadAd() {
        if(!(activity as MainActivity).isVipAccount){
            interstitialAd.adUnitId = INTERSTITIAL_ID
            val adRequest = AdRequest.Builder().build()
            interstitialAd.loadAd(adRequest)
        }
    }

    private fun initFinderEmojis() {
        emojis.add(binding.firstEmoji)
        emojis.add(binding.secondEmoji)
        emojis.add(binding.thirdEmoji)
        emojis.add(binding.fourthEmoji)
        emojis.add(binding.fifthEmoji)
    }

    private fun initAdapter() {
        userEmojisAdapter = GameRecyclerViewAdapter(GameRecyclerViewAdapter.OnEmojiClickListener{ emojiShopModel: EmojiShopModel?, position: Int ->
            handleClickedEmoji(emojiShopModel, position)
        })
        binding.gameEmojiFieldRv.adapter = userEmojisAdapter
    }

    private fun animateEmoji(emoji: EmojiShopModel?, position: Int, bottomEmoji: EmojiAppCompatEditText) {
        val newEmojiView: EmojiAppCompatButton? = createEmoji(emoji, position)
        val rootLocation = IntArray(2)
        val emojiRect = Rect()
        bottomEmoji.getGlobalVisibleRect(emojiRect)
        bottomEmoji.getLocationInWindow(rootLocation)
        val (x,y) = rootLocation
        newEmojiView?.animate()
            ?.translationX(x.toFloat() - 25)
            ?.translationY(y.toFloat())
            ?.alpha(0f)
            ?.setDuration(500)
            ?.withEndAction {
                activateStars()
            }
            ?.start()
    }

    private fun activateStars() {

    }

    private fun createEmoji(emoji: EmojiShopModel?, position: Int): EmojiAppCompatButton? {
        try {
            val emojiNew = EmojiAppCompatButton(requireContext())
            val emojiLocation = IntArray(2)
            binding.gameEmojiFieldRv[position].getLocationOnScreen(emojiLocation)
            val (x, y) = emojiLocation
            emojiNew.text = emoji?.text
            emojiNew.textSize = 26f
            emojiNew.x = x.toFloat() - 25
            emojiNew.y = y.toFloat()
            emojiNew.background = null
            emojiNew.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            rootArcadeGame.addView(emojiNew)
            return emojiNew
        } catch (e : Exception){
            e.printStackTrace()
            return null
        }
    }

    private fun handleClickedEmoji(it: EmojiShopModel?, position: Int) {
        for(emoji in emojis){
            if(it?.text == emoji.text.toString() && emoji.alpha != 1.0f){
                emoji.alpha = 1.0f
                animation.duration += 2
                increaseScore()
                playSound(MusicType.CORRECT)
                animateEmoji(it, position, emoji)
                findEmojis++
                break
            }
        }

        if(findEmojis == 5){
            binding.finderPlace.animate()
                .translationY(250f)
                .setDuration(500)
                .withEndAction {

                    createBottomEmojis(oneLevelEmojis)

                    binding.finderPlace
                        .animate()
                        .translationY(0f)
                        .setDuration(500)
                        .start()
                }
                .start()
            findEmojis = 0
        }
    }

    private fun playSound(type: MusicType) {
        (activity as MainActivity).mediaPlayerPool.play(type)
    }

    private fun stopSound(type: MusicType) {
        (activity as MainActivity).mediaPlayerPool.stop(MusicType.GAME)
    }

    private fun increaseScore() {
        binding.scoreText
            .animate()
            .scaleY(1.4f)
            .scaleX(1.4f)
            .withEndAction {
                score += 10
                binding.scoreText.text = (score).toString()
                binding.scoreText
                    .animate()
                    .scaleY(1.0f)
                    .scaleX(1.0f)
                    .setDuration(300)
                    .start()
            }.duration = 500
    }

    private fun setMusicSwitcher() {
        binding.arcadeGameAudioBtn.setOnClickListener {
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
            binding.arcadeGameAudioBtn.icon = resources.getDrawable(R.drawable.icons8_audio_24px)
        } else {
            binding.arcadeGameAudioBtn.icon = resources.getDrawable(R.drawable.icons8_no_audio_24px)
        }
    }

    private fun initViewModel() {
        allEmojis = (requireActivity() as MainActivity).randomEmojis as MutableList<EmojiShopModel?>
        createGamePlace(allEmojis)
        if(showGameHintPrefs.isArcadeHintShown()){
            animation.start()
        }
    }

    private fun createGamePlace(data: MutableList<EmojiShopModel?>) {
        val bottomEmojis = mutableListOf<EmojiShopModel?>()
        oneLevelEmojis = mutableListOf()
        userEmojisAdapter.clear()
        viewModel.countLevel(userEmojisAdapter, bottomEmojis, data, oneLevelEmojis)
    }

    private fun createBottomEmojis(list: MutableList<EmojiShopModel?>) {
        for(emoji in emojis){
            val random = list.random()
            emoji.setText(random?.text)
            emoji.alpha = 0.5f
        }
        binding.levelProgressBar.visibility = View.GONE
    }

}