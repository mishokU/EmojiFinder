package com.example.emojifinder.ui.account

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentAccountAvatarBinding
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.utils.ScreenSize
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountAvatarFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

//    @Inject
//    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
//    lateinit var viewModelShop : ShopViewModel

    lateinit var binding : FragmentAccountAvatarBinding
    var generatedList : HashMap<EmojiAppCompatButton, Boolean> = hashMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAvatarBinding.inflate(inflater)
        // Inflate the layout for this fragment

        viewModel = injectViewModel(viewModelFactory)
        //viewModelShop = injectViewModel(viewModelFactoryShop)

        setBackButton()

        loadAccountEmojis()
        loadShopEmojis()

        initGeneratorList()
        setGeneratorButtons()

        return binding.root
    }

    private fun initGeneratorList() {
        generatedList[binding.firstGeneratorField] = false
        generatedList[binding.secondGeneratorField] = false
        generatedList[binding.thirdGeneratorField] = false
        generatedList[binding.emojiAvatar] = false
    }

    private fun setGeneratorButtons() {
        binding.firstGeneratorField.setOnClickListener {
            changeButtonState(binding.firstGeneratorField)
        }

        binding.secondGeneratorField.setOnClickListener {
            changeButtonState(binding.secondGeneratorField)
        }

        binding.thirdGeneratorField.setOnClickListener {
            changeButtonState(binding.thirdGeneratorField)
        }
        binding.emojiAvatar.setOnClickListener {
            changeButtonState(binding.emojiAvatar)
        }
    }

    private fun changeButtonState(
        clickedButton: EmojiAppCompatButton
    ) {
        for(button in generatedList){
            if(button.key == clickedButton && !button.value){
                if(button.key == binding.emojiAvatar){
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.avatar_emoji_style_active
                    )
                }else {
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.generator_emoji_style_active
                    )
                }
                generatedList[button.key] = true
            }
            else {
                if(button.key == binding.emojiAvatar){
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.avatar_emoji_style
                    )
                } else {
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.generator_emoji_style
                    )
                }
                generatedList[button.key] = false
            }
        }
    }

    private fun loadShopEmojis() {

    }

    private fun loadAccountEmojis() {
        val emojiSize = ScreenSize.getEmojiSize(resources)
        for(i : Int in 0..2){

            val horizontal = LinearLayout(requireContext())
            horizontal.gravity = Gravity.CENTER_HORIZONTAL

            for(j : Int in 0..7){

                val buttonsParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    100,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val emojiView = EmojiAppCompatButton(requireContext())
                emojiView.isSingleLine = true
                emojiView.layoutParams = buttonsParams
                emojiView.background = resources.getDrawable(R.drawable.emoji_style)

                emojiView.textSize = emojiSize

                emojiView.text = Emoji.getRandomEmoji()
                emojiView.setPadding(0,0,0,0)

                emojiView.setOnClickListener {
                    handleEmojiNavigator(it as EmojiAppCompatButton)
                }

                horizontal.addView(emojiView)
            }
            binding.yourEmojisPlace.addView(horizontal)
        }
    }

    private fun handleEmojiNavigator(it: EmojiAppCompatButton) {
        for(button in generatedList){
            if(button.value)
            {
                button.key.text = it.text
            }
        }
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAvatar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAvatar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
