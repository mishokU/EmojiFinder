package com.example.emojifinder.ui.game.arcade

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.ArcadeGameLayoutBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ShopViewModel
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

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArcadeGameLayoutBinding.inflate(inflater)

        initTimer()
        initFinderEmojis()
        initAdapter()
        initViewModel()

        return binding.root
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

        })
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