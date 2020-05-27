package com.example.emojifinder.ui.boxes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.databinding.FragmentLootBoxesBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.utils.EmojiCost
import com.example.emojifinder.ui.utils.ScreenSize
import com.example.emojifinder.ui.utils.closeChestPlaceholder
import com.example.emojifinder.ui.utils.openChestPlaceholder
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.random.Random


class LootBoxesFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    lateinit var binding : FragmentLootBoxesBinding
    lateinit var adapter : LootBoxRecyclerViewAdapter
    lateinit var values : AccountValuesModel

    var open : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLootBoxesBinding.inflate(inflater)

        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModel = injectViewModel(viewModelFactory)

        binding.lifecycleOwner = this

        getValuesFromBundle()

        initAdapter()
        initShopViewModel()

        initBuyChestButton()

        setBackButton()

        return binding.root
    }

    private fun initBuyChestButton() {
        binding.buyChestBtn.setOnClickListener {
            val emos = binding.emosCount.text.toString().toInt()
            val boxes = binding.boxesCount.text.toString().toInt()
            if (emos >= 200) {
                viewModel.updateUserEmos(emos - 200)
                viewModel.updateUserBoxes(boxes + 1)

                binding.emosCount.text = (emos - 200).toString()
                binding.boxesCount.text = (boxes + 1).toString()
            } else {
                binding.buyChestBtn.text = resources.getString(R.string.not_enough_emos)
            }
        }
    }

    private fun getValuesFromBundle() {
        values = LootBoxesFragmentArgs.fromBundle(requireArguments()).Values
        binding.values = values
    }

    private fun initWinningEmojiButtons(emojiShopModel: EmojiShopModel) {
        binding.sellWinningEmoji.text = EmojiCost.emojiSellCost(emojiShopModel)
        val cost = EmojiCost.getEmojiSellCost(binding.sellWinningEmoji)
        binding.takeWinningEmoji.setOnClickListener {

            binding.emojiText.visibility = View.INVISIBLE
            emojiWinButtons.visibility = View.INVISIBLE

            hideEmoji(-90f)

            viewModel.addEmoji(emojiShopModel,0,getUserValues())

            updateUserEmojisCount()

        }

        binding.sellWinningEmoji.setOnClickListener {

            binding.emojiText.visibility = View.INVISIBLE
            emojiWinButtons.visibility = View.INVISIBLE

            hideEmoji(90f)

            val emos = binding.emosCount.text.toString().toInt() + cost

            viewModel.updateUserEmos(emos)

            binding.emosCount.text = emos.toString()

        }
    }


    companion object ScrollListener : RecyclerView.OnScrollListener() {

        private lateinit var animatedView : EmojiAppCompatButton
        private lateinit var boxPlace: RelativeLayout
        private lateinit var openChestPlaceholder: View
        private lateinit var emojiText: LinearLayout
        private lateinit var emojiWinButtons: LinearLayout

        operator fun invoke(
            animatedView: EmojiAppCompatButton,
            boxPlace: RelativeLayout,
            openChestPlaceholder: View,
            emojiText: LinearLayout,
            emojiWinButtons: LinearLayout
        ): ScrollListener {
            this.animatedView = animatedView
            this.boxPlace = boxPlace
            this.openChestPlaceholder = openChestPlaceholder
            this.emojiText = emojiText
            this.emojiWinButtons = emojiWinButtons
            return this
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                SCROLL_STATE_IDLE ->  {
                    recyclerView.removeOnScrollListener(this)
                    animateView()
                }
            }
        }

        private fun animateView() {
            animatedView.visibility = View.VISIBLE
            animatedView.animate()
                .scaleXBy(0f)
                .scaleYBy(0f)
                .scaleX(2f)
                .scaleY(2f)
                .translationX(-350f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        emojiText.visibility = View.VISIBLE
                        emojiWinButtons.visibility = View.VISIBLE
                    }
                })
                .setInterpolator(AccelerateDecelerateInterpolator()).duration = 1000

            openChestPlaceholder(boxPlace, openChestPlaceholder)
        }

    }

    private fun initOpenBoxButton() {
        binding.rollEmojiBtn.setOnClickListener {
            if(binding.boxesCount.text.toString().toInt() > 0) {
                binding.chestRv.post {
                    run {
                        binding.chestRv.removeOnScrollListener(ScrollListener)
                        binding.chestRv.addOnScrollListener(
                            ScrollListener(
                                binding.animatedView,
                                binding.boxPlace,
                                binding.openChestPlaceholder,
                                binding.emojiText,
                                binding.emojiWinButtons
                            )
                        )

                        val size = adapter.currentList.size / 4
                        val random = Random.nextInt(0, size)
                        val emoji = adapter.currentList[random]

                        initWinningEmojiButtons(emoji)

                        binding.emojiName.text = emoji.name
                        binding.emojiGroup.text = emoji.group

                        binding.chestRv.smoothScrollToPosition(random)
                        binding.animatedView.text = adapter.currentList[random].text
                    }
                }
                updateUserBoxes()
            } else {
                binding.buyChestBtn.text = resources.getString(R.string.not_enough_emos)
            }
        }

        binding.animatedView.setOnClickListener {
            if(!open){
                bounce(90f)
                open = true
            }
        }
    }



    private fun updateUserEmojisCount(){
        val count = binding.emojisCount.text.toString().toInt() + 1
        binding.emojisCount.text = count.toString()
    }

    private fun updateUserBoxes() {
        val boxesCount = binding.boxesCount.text.toString().toInt() - 1
        viewModel.updateUserBoxes(boxesCount)
        binding.boxesCount.text = (boxesCount).toString()
    }

    private fun hideEmoji(yAxes : Float) {
        animatedView.animate()
            .scaleXBy(0f)
            .scaleYBy(0f)
            .scaleX(-1f)
            .scaleY(-1f)
            .setListener(null)
            .translationX(350f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .duration = 1000

        bounce(yAxes)
    }


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onStart() {
        super.onStart()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    private fun initAdapter() {
        adapter = LootBoxRecyclerViewAdapter(LootBoxRecyclerViewAdapter.OnShopItemClickListener{

        })
        binding.chestRv.adapter = adapter
    }

    private fun initShopViewModel() {
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                    //    binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                      //  binding.progressBar.visibility = View.INVISIBLE

                        adapter.submitList(it.data)

                        initOpenBoxButton()
                        //generateGroupChips(it.data)
                    }
                    is Result.Error -> {
                    //    binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    private fun bounce(yAxes : Float){

        binding.animatedView.clearAnimation()

        val transAnim = TranslateAnimation(
            0F, 0F, 0F, ScreenSize.dipToPixels(requireContext(), yAxes)
        )

        transAnim.duration = 1000
        transAnim.fillAfter = true
        transAnim.interpolator = BounceInterpolator()
        transAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                binding.animatedView.clearAnimation()

                val left: Int = binding.animatedView.left
                val top: Int = binding.animatedView.top
                val right: Int = binding.animatedView.right
                val bottom: Int = binding.animatedView.bottom

                binding.animatedView.visibility = View.INVISIBLE

                binding.animatedView.layout(left, top, right, bottom)
            }

            override fun onAnimationStart(animation: Animation?) {
                closeChestPlaceholder(binding.boxPlace, binding.openChestPlaceholder)
            }
        })
        binding.animatedView.startAnimation(transAnim)
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.LootboxToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as AppCompatActivity).supportActionBar?.title = ""

        binding.LootboxToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun getUserValues(): AccountValuesModel {
        return AccountValuesModel(
            emos = binding.emosCount.text.toString().toInt(),
            boxes = binding.boxesCount.text.toString().toInt(),
            emojis = binding.emojisCount.text.toString().toInt()
        )
    }

}
