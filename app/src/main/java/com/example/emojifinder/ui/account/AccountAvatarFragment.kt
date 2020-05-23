package com.example.emojifinder.ui.account

import android.os.Bundle
import android.view.*
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
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.shop.EmojisRecyclerViewAdapter
import com.example.emojifinder.ui.shop.ShopEmojiDialog
import com.example.emojifinder.ui.utils.closeFilters
import com.example.emojifinder.ui.utils.openFilters
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountAvatarFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    lateinit var adapter : EmojisRecyclerViewAdapter
    lateinit var userEmojisAdapter : EmojisRecyclerViewAdapter

    lateinit var binding : FragmentAccountAvatarBinding
    lateinit var profile : MainAccountModel

    var generatedList : HashMap<EmojiAppCompatButton, Boolean> = hashMapOf()
    var isFilterVisible : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAvatarBinding.inflate(inflater)
        // Inflate the layout for this fragment

        viewModel = injectViewModel(viewModelFactory)
        viewModelShop = injectViewModel(viewModelFactoryShop)

        setBackButton()
        getUserMainData()

        initGeneratorList()
        setGeneratorButtons()

        initUserEmojisAdapter()
        initShopAdapter()

        initUserEmojisViewModel()
        initUserValuesViewModel()
        initShowFilterButton()
        initUserMainData()

        initGeneratorLeftRightButtons()
        initAvatarLeftRightButtons()

        return binding.root
    }

    private fun getUserMainData() {
        if(requireArguments().containsKey("MainInfo")){
            profile = AccountAvatarFragmentArgs.fromBundle(requireArguments()).MainInfo
            binding.profile = profile
        }
        else {
            viewModel.fetchMainUserData()
        }
    }


    private fun initAvatarLeftRightButtons() {
        binding.leftChangeAvatarBtn.setOnClickListener {

        }
        binding.rightChangeAvatarBtn.setOnClickListener {

        }
        binding.saveAvatarBtn.setOnClickListener {
            viewModel.updateUserAvatar(binding.emojiAvatar.text.toString())
            viewModel.fetchMainUserData()
        }
    }

    private fun initGeneratorLeftRightButtons() {
        binding.leftChangeGeneratorBtn.setOnClickListener {
            for(button in generatedList){
                if(button.value){
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
                        binding.emojiAvatar -> {
                            changeButtonState(binding.thirdGeneratorField)
                        }
                    }
                    break
                }
            }
        }

        binding.rightChangeGeneratorBtn.setOnClickListener {
            for(button in generatedList){
                if(button.value) {
                    when (button.key) {
                        binding.firstGeneratorField -> {
                            changeButtonState(binding.secondGeneratorField)
                        }
                        binding.secondGeneratorField -> {
                            changeButtonState(binding.thirdGeneratorField)
                        }
                        binding.thirdGeneratorField -> {
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
    }

    private fun setCheckedFilters() {
        val categories : MutableList<String> = mutableListOf()
        for(chip in binding.categoriesChipGroup.children){
            if((chip as Chip).isChecked){
                binding.categoriesChipGroup.removeView(chip)
                binding.selectedFilters.addView(chip)
                categories.add(chip.text.toString())
            }
            adapter.filter(categories)
        }
    }

    private fun handleFilters() {
        isFilterVisible = if(!isFilterVisible){
            openFilters(binding.shopRecyclerView, binding.filtersPlace)
            binding.filterToggleButton
                .setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.icons8_filter_100px_close))
            true
        } else {
            closeFilters(binding.shopRecyclerView, binding.filtersPlace)
            binding.filterToggleButton
                .setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.icons8_filter_100px))
            false
        }
    }

    private fun initUserEmojisViewModel(){
        viewModel.fetchUserEmojis()
        viewModel.userEmojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let { userEmojis ->
                when(userEmojis){
                    is Result.Loading -> {
                        binding.userEmojisProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.userEmojisProgressBar.visibility = View.INVISIBLE
                        userEmojisAdapter.submitList(userEmojis.data)

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

    private fun initUserMainData(){
        viewModel.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.loadingAvatar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingAvatar.visibility = View.GONE
                        profile = it.data!!
                        binding.profile = it.data
                    }
                }
            }
        })
    }

    private fun initUserValuesViewModel() {
        viewModel.fetchUserValues()
        viewModel.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { values ->
                when(values){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        binding.values = values.data
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun initShopViewModel(data: List<EmojiShopModel?>) {
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        adapter.shopSubmitList(it.data, data)
                        generateGroupChips(it.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    private fun generateGroupChips(data: List<EmojiShopModel?>) {
        val groups = data.distinctBy {
            it?.group
        }
        for(model in groups){
            val chip = Chip(requireContext())
            chip.text = model?.group
            chip.isChipIconVisible = true
            chip.isClickable = true
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { _, isChecked ->
                if(!isChecked && chip.parent == binding.selectedFilters){
                    binding.selectedFilters.removeView(chip)
                    binding.categoriesChipGroup.addView(chip)
                }
            }

            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun initShopAdapter() {
        adapter = EmojisRecyclerViewAdapter(EmojisRecyclerViewAdapter.OnShopItemClickListener{
            showEmojiPriceAlert(it)
        }, binding.progressBar,true)
        binding.shopRecyclerView.adapter = adapter
    }

    private fun showEmojiPriceAlert(emoji: EmojiShopModel?) {
        ShopEmojiDialog.showAlert(this, emoji)
        ShopEmojiDialog.getBuyButton().setOnClickListener {
            val cost = ShopEmojiDialog.getEmojiBuyCost()
            val userValues = getUserValues()

            if(cost < userValues.emos){
                viewModel.addEmoji(emoji,
                    ShopEmojiDialog.getEmojiBuyCost(),
                    getUserValues()
                )

                viewModel.fetchUserEmojis()
                viewModel.fetchUserValues()
            } else {
                Toast.makeText(requireContext(),resources.getString(R.string.not_enough_emos), Toast.LENGTH_LONG).show()
            }

            ShopEmojiDialog.dialogView.dismiss()
        }
    }

    private fun initUserEmojisAdapter(){
        userEmojisAdapter = EmojisRecyclerViewAdapter(EmojisRecyclerViewAdapter.OnShopItemClickListener{
            handleEmojiNavigator(it)
        }, binding.progressBar,false)
        binding.userEmojisPlaceRv.adapter = userEmojisAdapter
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

        binding.generateEmojiSet.setOnClickListener {
            val first = binding.firstGeneratorField.text.toString()
            val second = binding.secondGeneratorField.text.toString()
            val third = binding.thirdGeneratorField.text.toString()

            val generatedEmoji = "$first$second$third"

            changeResultState(generatedEmoji)
        }

        binding.resetEmojisBtn.setOnClickListener {
            binding.firstGeneratorField.text = ""
            binding.secondGeneratorField.text = ""
            binding.thirdGeneratorField.text = ""
        }
    }

    private fun changeResultState(generatedEmoji: String) {
        binding.resultGeneratorField.text = generatedEmoji
        if(binding.resultGeneratorField.lineCount == 1){
            binding.resultGeneratorField.visibility = View.VISIBLE
            viewModel.addGeneratedEmoji(adapter.getGeneratedEmoji(generatedEmoji))

            viewModel.fetchUserEmojis()
            viewModel.fetchUserValues()

        } else {
            binding.resultGeneratorField.text = ""
            binding.resultGeneratorField.visibility = View.INVISIBLE
            binding.failedToGenerate.playAnimation()
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

    private fun handleEmojiNavigator(emoji: EmojiShopModel?) {
        if(generatedList.containsValue(value = true)){
            for(button in generatedList){
                if(button.value)
                {
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

                viewModel.removeEmoji(emoji,
                    ShopEmojiDialog.getEmojiSellCost(),
                    getUserValues())

                viewModel.fetchUserEmojis()
                viewModel.fetchUserValues()

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
