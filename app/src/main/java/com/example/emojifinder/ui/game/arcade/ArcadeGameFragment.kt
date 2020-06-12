package com.example.emojifinder.ui.game.arcade

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.addListener
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.ArcadeGameLayoutBinding
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MusicType
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.game.campaign.gameAlerts.ExitGameDialog
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.shop.EmojiShopModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ArcadeGameFragment : DaggerFragment() {

    private lateinit var binding : ArcadeGameLayoutBinding
    lateinit var userEmojisAdapter : GameRecyclerViewAdapter
    private lateinit var animation: ObjectAnimator

    var emojis : MutableList<EmojiAppCompatEditText> = mutableListOf()

    lateinit var allEmojis : MutableList<EmojiShopModel?>
    lateinit var oneLevelEmojis : MutableList<EmojiShopModel?>

    var findEmojis = 0
    var score = 0
    var userScore = 0
    var userEmos = 0

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    @Inject
    lateinit var viewModelFactoryAccount: ViewModelProvider.Factory
    lateinit var viewModelAccount : AccountViewModel

    @Inject
    lateinit var settingsPrefs: SettingsPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArcadeGameLayoutBinding.inflate(inflater)


        viewModelAccount = injectViewModel(viewModelFactoryAccount)

        EndGameDialog.create(this)

        initEndGameButtons()

        initButtons()

        initTimer()
        initFinderEmojis()
        initAdapter()
        initViewModel()

        loadUserScore()

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
    }

    private fun onBackButton() {
        animation.pause()

        ExitGameDialog.create(this)
        ExitGameDialog.setMusicSwitcher(settingsPrefs)

        ExitGameDialog.open()

        ExitGameDialog.getGameExitButton().setOnClickListener {
            ExitGameDialog.dialogView.dismiss()
            animation.removeAllListeners()
            animation.cancel()
            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
            this.findNavController().navigateUp()
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
            EndGameDialog.dialogView.dismiss()
        }

        EndGameDialog.getExitButton().setOnClickListener {
            EndGameDialog.dialogView.dismiss()
            this.findNavController().navigateUp()
        }
    }

    private fun initTimer() {
        animation = ObjectAnimator
            .ofInt(binding.gameProgressBar, "progress", 60, 0)
        animation.duration = (60 * 1000).toLong()
        animation.interpolator = LinearInterpolator()

        animation.addUpdateListener {
            binding.gameTime.text = it.animatedValue.toString()
        }

        animation.addListener(onEnd = {
            openAdvertisement()
            playSound(MusicType.WIN)
            stopSound(MusicType.GAME)
            if(score == 0){
                EndGameDialog.open(score)
            } else {
                EndGameDialog.open(score)
                viewModelAccount.updateScore(score + userScore)
                viewModelAccount.updateUserEmos(userEmos + score / 5)
            }
        })
    }

    private fun openAdvertisement() {

    }

    private fun initFinderEmojis() {
        emojis.add(binding.firstEmoji)
        emojis.add(binding.secondEmoji)
        emojis.add(binding.thirdEmoji)
        emojis.add(binding.fourthEmoji)
        emojis.add(binding.fifthEmoji)
    }

    private fun initAdapter() {
        userEmojisAdapter = GameRecyclerViewAdapter(GameRecyclerViewAdapter.OnEmojiClickListener{
            handleClickedEmoji(it)
        })
        binding.gameEmojiFieldRv.adapter = userEmojisAdapter
    }

    private fun handleClickedEmoji(it: EmojiShopModel?) {
        for(emoji in emojis){
            if(it?.text == emoji.text.toString() && emoji.alpha != 1.0f){
                emoji.alpha = 1.0f
                animation.duration += 2
                increaseScore()
                playSound(MusicType.CORRECT)
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

    private fun initViewModel() {
        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.levelProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.levelProgressBar.visibility = View.INVISIBLE

                        allEmojis = it.data as MutableList<EmojiShopModel?>
                        createGamePlace(allEmojis)
                        animation.start()
                        //playSound(MusicType.GAME)
                    }

                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun createGamePlace(data: MutableList<EmojiShopModel?>) {
        val bottomEmojis = mutableListOf<EmojiShopModel?>()
        oneLevelEmojis = mutableListOf()
        userEmojisAdapter.submitList(emptyList())
        for(i : Int in 0..44){
            val emoji = data.random()
            oneLevelEmojis.add(emoji)
            bottomEmojis.add(emoji)
        }
        userEmojisAdapter.submitList(oneLevelEmojis)
        createBottomEmojis(bottomEmojis)
    }

    private fun createBottomEmojis(list: MutableList<EmojiShopModel?>) {
        for(emoji in emojis){
            val random = list.random()
            emoji.setText(random?.text)
            emoji.alpha = 0.5f
        }

    }

}