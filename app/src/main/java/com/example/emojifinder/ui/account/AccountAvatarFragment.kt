package com.example.emojifinder.ui.account

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.databinding.FragmentAccountAvatarBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MusicType
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.ui.boxes.EmojisBoxDialog
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.shop.EmojisRecyclerViewAdapter
import com.example.emojifinder.ui.shop.ShopEmojiDialog
import com.example.emojifinder.ui.utils.EmojiCost
import com.example.emojifinder.ui.utils.closeFilters
import com.example.emojifinder.ui.utils.openFilters
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountAvatarFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    lateinit var adapter: EmojisRecyclerViewAdapter
    lateinit var userEmojisAdapter: EmojisRecyclerViewAdapter

    lateinit var binding: FragmentAccountAvatarBinding
    lateinit var profile: MainAccountModel
    lateinit var userEmojisCount: String

    var generatedList: HashMap<EmojiAppCompatButton, Boolean> = hashMapOf()

    var isFilterVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAvatarBinding.inflate(inflater)
        // Inflate the layout for this fragment

        viewModel = injectViewModel(viewModelFactory)

        EmojisBoxDialog.create(this)

        initUserEmojisAdapter()
        initShopAdapter()

        setBackButton()
        getUserMainData()

        initGeneratorList()
        setGeneratorButtons()

        initShowFilterButton()

        initUserMainData()

        initGeneratorLeftRightButtons()
        initAvatarLeftRightButtons()

        initValues()

        return binding.root
    }

    private fun getUserMainData() {
        if (requireArguments().containsKey("MainInfo")) {
            profile = AccountAvatarFragmentArgs.fromBundle(requireArguments()).MainInfo
            binding.profile = profile
            initUserEmojisViewModel()
        } else {
            viewModel.fetchMainUserData()
        }
    }

    private fun initValues() {
        binding.emojiBoxEt.setText("\uD83C\uDF9F️")
        binding.emosEt.setText("\uD83D\uDCB0")
        binding.emojiEt.setText("\uD83D\uDE00")
    }


    private fun initAvatarLeftRightButtons() {
        binding.leftChangeAvatarBtn.setOnClickListener {

        }

        binding.rightChangeAvatarBtn.setOnClickListener {

        }

        binding.saveAvatarBtn.setOnClickListener {
            viewModel.updateUserAvatar(binding.emojiAvatar.text.toString())
            Toast.makeText(requireContext(), "Avatar updated !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initGeneratorLeftRightButtons() {
        binding.leftChangeGeneratorBtn.setOnClickListener {
            for (button in generatedList) {
                if (button.value) {
                    when (button.key) {
                        binding.firstGeneratorField -> {
                            changeButtonState(binding.emojiAvatar)
                        }
                        binding.secondGeneratorField -> {
                            changeButtonState(binding.firstGeneratorField)
                        }
                        binding.thirdGeneratorField -> {
                            changeButtonState(binding.secondGeneratorField)
                        }

                        binding.fourthGeneratorField -> {
                            changeButtonState(binding.thirdGeneratorField)
                        }

                        binding.fifthGeneratorField -> {
                            changeButtonState(binding.fourthGeneratorField)
                        }

                        binding.sixGeneratorField -> {
                            changeButtonState(binding.fifthGeneratorField)
                        }

                        binding.emojiAvatar -> {
                            changeButtonState(binding.thirdGeneratorField)
                        }
                    }
                    break
                }
            }
        }

        binding.rightChangeGeneratorBtn.setOnClickListener {
            for (button in generatedList) {
                if (button.value) {
                    when (button.key) {
                        binding.firstGeneratorField -> {
                            changeButtonState(binding.secondGeneratorField)
                        }
                        binding.secondGeneratorField -> {
                            changeButtonState(binding.thirdGeneratorField)
                        }
                        binding.thirdGeneratorField -> {
                            changeButtonState(binding.fourthGeneratorField)
                        }

                        binding.fourthGeneratorField -> {
                            changeButtonState(binding.fifthGeneratorField)
                        }

                        binding.fifthGeneratorField -> {
                            changeButtonState(binding.sixGeneratorField)
                        }

                        binding.sixGeneratorField -> {
                            changeButtonState(binding.emojiAvatar)
                        }

                        binding.emojiAvatar -> {
                            changeButtonState(binding.firstGeneratorField)
                        }
                    }
                    break
                }
            }
        }
    }

    private fun initShowFilterButton() {
        binding.filterToggleButton.setOnClickListener {
            handleFilters()
        }
        binding.applyFilters.setOnClickListener {
            setCheckedFilters()
            handleFilters()
        }
        binding.resetFilters.setOnClickListener {
            adapter.resetFilters()
            handleFilters()
        }
        binding.showChest.setOnClickListener {
            this.findNavController().navigate(
                AccountAvatarFragmentDirections.actionAccountAvatarFragmentToLootBoxesFragment(
                    getUserValues()
                )
            )
        }
        binding.failedToGenerate.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationEnd(animation: Animator?) {
                binding.resultGeneratorField.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun setCheckedFilters() {
        val categories: MutableList<String> = mutableListOf()
        for (chip in binding.categoriesChipGroup.children) {
            if ((chip as Chip).isChecked) {
                binding.categoriesChipGroup.removeView(chip)
                binding.selectedFilters.addView(chip)
                categories.add(chip.text.toString())
            }
            adapter.filter(categories)
        }
    }

    private fun handleFilters() {
        isFilterVisible = if (!isFilterVisible) {
            openFilters(binding.shopRecyclerView, binding.filtersPlace, 100, 200)
            binding.filterToggleButton
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icons8_filter_100px_close
                    )
                )
            true
        } else {
            closeFilters(binding.shopRecyclerView, binding.filtersPlace, 100, 200)
            binding.filterToggleButton
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icons8_filter_100px
                    )
                )
            false
        }
    }

    private fun initUserEmojisViewModel() {
        viewModel.fetchUserEmojis()
        viewModel.userEmojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let { userEmojis ->
                when (userEmojis) {
                    is Result.Loading -> {
                        binding.userEmojisProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.userEmojisProgressBar.visibility = View.INVISIBLE

                        userEmojisAdapter.submitUserList(profile.avatar, userEmojis.data)
                        userEmojisCount = userEmojis.data.size.toString()

                        initUserValuesViewModel()
                        initShopViewModel(userEmojis.data)
                    }
                    is Result.Error -> {
                        binding.userEmojisProgressBar.visibility = View.INVISIBLE
                        binding.errorMessage.text = userEmojis.exception.message
                    }
                }
            }
        })
    }

    private fun initUserMainData() {
        viewModel.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loadingAvatar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingAvatar.visibility = View.GONE
                        profile = it.data!!
                        binding.profile = it.data

                        initUserEmojisViewModel()
                    }
                }
            }
        })
    }

    private fun initUserValuesViewModel() {
        viewModel.fetchUserValues()
        viewModel.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { values ->
                when (values) {
                    is Result.Success -> {
                        binding.emosCount.text = values.data.emos.toString()
                        binding.boxesCount.text = values.data.boxes.toString()
                        binding.emojisCount.text = userEmojisCount
                    }
                }
            }
        })
    }

    private fun initShopViewModel(data: List<EmojiShopModel?>) {
        adapter.shopSubmitList((requireActivity() as MainActivity).randomEmojis, data)
        generateGroupChips((requireActivity() as MainActivity).randomEmojis)
    }

    @Suppress("DEPRECATION")
    private fun generateGroupChips(data: List<EmojiShopModel?>) {
        binding.categoriesChipGroup.removeAllViews()
        val groups = data.distinctBy {
            it?.group
        }
        for (model in groups) {
            val chip = Chip(requireContext())
            chip.text = model?.group
            chip.isChipIconVisible = true
            chip.isClickable = true
            chip.isCheckable = true
            chip.setTextColor(resources.getColor(R.color.background_color))
            chip.chipIconTint = resources.getColorStateList(R.color.background_color)
            chip.chipBackgroundColor = resources.getColorStateList(R.color.blue_color)

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked && chip.parent == binding.selectedFilters) {
                    binding.selectedFilters.removeView(chip)
                    binding.categoriesChipGroup.addView(chip)
                }
            }
            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun initShopAdapter() {
        adapter = EmojisRecyclerViewAdapter(EmojisRecyclerViewAdapter.OnShopItemClickListener {
            showEmojiPriceAlert(it)
        }, binding.progressBar, true)
        binding.shopRecyclerView.adapter = adapter
    }

    private fun showEmojiPriceAlert(emoji: EmojiShopModel?) {
        ShopEmojiDialog.showAlert(this, emoji)
        ShopEmojiDialog.getBuyButton().setOnClickListener {
            val cost = EmojiCost.getEmojiBuyCost(ShopEmojiDialog.getBuyButton())
            val userValues = getUserValues()

            if (cost < userValues.emos) {
                viewModel.addEmoji(
                    emoji,
                    EmojiCost.getEmojiBuyCost(ShopEmojiDialog.getBuyButton()),
                    getUserValues()
                )

                viewModel.fetchUserEmojis()
                viewModel.fetchUserValues()

                (activity as MainActivity).mediaPlayerPool.play(MusicType.BUY)
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.not_enough_emos),
                    Toast.LENGTH_LONG
                ).show()
            }
            ShopEmojiDialog.dialogView.dismiss()
        }
    }

    private fun initUserEmojisAdapter() {
        userEmojisAdapter =
            EmojisRecyclerViewAdapter(EmojisRecyclerViewAdapter.OnShopItemClickListener {
                handleEmojiNavigator(it)
            }, binding.progressBar, false)
        binding.userEmojisPlaceRv.adapter = userEmojisAdapter
    }

    private fun initGeneratorList() {
        generatedList[binding.firstGeneratorField] = false
        generatedList[binding.secondGeneratorField] = false
        generatedList[binding.thirdGeneratorField] = false
        generatedList[binding.fourthGeneratorField] = false
        generatedList[binding.fifthGeneratorField] = false
        generatedList[binding.sixGeneratorField] = false

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

        binding.fourthGeneratorField.setOnClickListener {
            changeButtonState(binding.fourthGeneratorField)
        }

        binding.fifthGeneratorField.setOnClickListener {
            changeButtonState(binding.fifthGeneratorField)
        }

        binding.sixGeneratorField.setOnClickListener {
            changeButtonState(binding.sixGeneratorField)
        }

        binding.emojiAvatar.setOnClickListener {
            changeButtonState(binding.emojiAvatar)
        }

        binding.generateEmojiSet.setOnClickListener {
            val first = binding.firstGeneratorField.text.toString()
            val second = binding.secondGeneratorField.text.toString()
            val third = binding.thirdGeneratorField.text.toString()
            val fourth = binding.fourthGeneratorField.text.toString()
            val fifth = binding.fifthGeneratorField.text.toString()
            val sixth = binding.sixGeneratorField.text.toString()

            val generatedEmoji = "$first$second$third$fourth$fifth$sixth"

            changeResultState(generatedEmoji)
        }

        binding.resetEmojisBtn.setOnClickListener {
            binding.firstGeneratorField.text = ""
            binding.secondGeneratorField.text = ""
            binding.thirdGeneratorField.text = ""
            binding.fourthGeneratorField.text = ""
            binding.fifthGeneratorField.text = ""
            binding.sixGeneratorField.text = ""
        }
    }

    private fun changeResultState(generatedEmoji: String) {
        binding.resultGeneratorField.text = generatedEmoji
        if (binding.resultGeneratorField.lineCount == 1 && generatedEmoji != "" && generatedEmoji.length > 1) {
            binding.resultGeneratorField.visibility = View.VISIBLE
            viewModel.addGeneratedEmoji(adapter.getGeneratedEmoji(generatedEmoji))
            viewModel.fetchUserEmojis()
            viewModel.fetchUserValues()

            (activity as MainActivity).mediaPlayerPool.play(MusicType.SUCCESSFUL)

        } else {
            binding.resultGeneratorField.text = ""
            binding.resultGeneratorField.visibility = View.INVISIBLE
            binding.failedToGenerate.playAnimation()

            (activity as MainActivity).mediaPlayerPool.play(MusicType.FAIL)
        }
    }

    private fun changeButtonState(
        clickedButton: EmojiAppCompatButton
    ) {
        for (button in generatedList) {
            if (button.key == clickedButton && !button.value) {
                if (button.key == binding.emojiAvatar) {
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.avatar_emoji_style_active
                    )
                } else {
                    button.key.background = ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.generator_emoji_style_active
                    )
                }
                generatedList[button.key] = true
            } else {
                if (button.key == binding.emojiAvatar) {
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

    private fun handleEmojiNavigator(emoji: EmojiShopModel?) {
        if (generatedList.containsValue(value = true)) {
            for (button in generatedList) {
                if (button.value) {
                    button.key.text = emoji!!.text
                }
            }
        } else {
            sellEmoji(emoji)
        }
    }

    private fun sellEmoji(emoji: EmojiShopModel?) {
        ShopEmojiDialog.showUserAlert(this, emoji)
        ShopEmojiDialog.getYesSaleButton().setOnClickListener {
            if (emoji != null) {

                viewModel.removeEmoji(
                    emoji,
                    EmojiCost.getEmojiSellCost(ShopEmojiDialog.getSaleButton()),
                    getUserValues()
                )

                viewModel.fetchUserEmojis()
                viewModel.fetchUserValues()

                (activity as MainActivity).mediaPlayerPool.play(MusicType.MONEY)

                ShopEmojiDialog.dialogUserView.dismiss()
            }
        }
    }

    private fun getUserValues(): AccountValuesModel {
        return AccountValuesModel(
            emos = binding.emosCount.text.toString().toInt(),
            boxes = binding.boxesCount.text.toString().toInt(),
            emojis = binding.emojisCount.text.toString().toInt()
        )
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAvatar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAvatar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
