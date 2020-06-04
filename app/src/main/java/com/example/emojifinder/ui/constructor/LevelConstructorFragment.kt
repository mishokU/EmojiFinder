package com.example.emojifinder.ui.constructor

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.ViewModelFactory
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentLevelConstructorBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ConstructorViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.constructor.dialogs.ExitLevelDialog
import com.example.emojifinder.ui.constructor.dialogs.ResetLevelDialog
import com.example.emojifinder.ui.constructor.dialogs.SaveLevelDialog
import com.example.emojifinder.ui.constructor.dialogs.SentLevelDialog
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.utils.closeFilters
import com.example.emojifinder.ui.utils.openFilters
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.emoji_constructor_item.view.*
import javax.inject.Inject


class LevelConstructorFragment : DaggerFragment() {

    private lateinit var binding : FragmentLevelConstructorBinding
    private lateinit var allEmojisAdapter : AllEmojisRecyclerViewAdapter
    private lateinit var levelAdapter : LevelConstructorRecyclerViewAdapter

    private var isGridActive : Boolean = true
    private var isFilterVisible : Boolean = false
    private lateinit var saveItemIcon : MenuItem
    private lateinit var level : SmallLevelModel

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel : ConstructorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLevelConstructorBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)

        ExitLevelDialog.create(this)
        ResetLevelDialog.create(this)
        SaveLevelDialog.create(this)
        SentLevelDialog.create(this)

        setBackButton()


        initDialogButtons()
        initAllEmojisAdapter()
        initConstructorAdapter()

        getAllEmojisFromJson()

        getLevel()

        initButtons()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun getLevel() {
        if(requireArguments().containsKey("Level")) {
            level = LevelConstructorFragmentArgs.fromBundle(requireArguments()).Level
            SaveLevelDialog.setLevel(level)
            fetchLevel(level)
        } else {
            getLevelEmojis()
        }
    }

    private fun fetchLevel(level: SmallLevelModel) {
        viewModel.levelTitle = level.title
        viewModel.emojis.observe(viewLifecycleOwner, Observer {
            levelAdapter.submitList(it)
        })
    }


    private fun initDialogButtons() {
        ResetLevelDialog.getResetLevelBtn().setOnClickListener {
            resetEmojis()
            ResetLevelDialog.dialogView.dismiss()
        }

        SaveLevelDialog.getSaveLevelBtn().setOnClickListener {
            if(SaveLevelDialog.isNotEmpty()){

                if(SaveLevelDialog.getSmallLevelModel() != null){

                    levelAdapter.setLevelTitleToEmojis(SaveLevelDialog.getNameLabel().text.toString())

                    viewModel.saveLevel(
                        levelAdapter.currentList,
                        SaveLevelDialog.getSmallLevelModel()!!
                    )

                    saveItemIcon.icon = ContextCompat.getDrawable(requireContext(),
                        R.drawable.icons8_save_26px_green)

                    SaveLevelDialog.dialogView.dismiss()
                }
            }
        }

        SentLevelDialog.getSentLevelBtn().setOnClickListener {

        }

        ExitLevelDialog.getSaveLevelBtn().setOnClickListener {
            ExitLevelDialog.dialogView.dismiss()
            SaveLevelDialog.open()
        }

    }

    private fun initButtons() {
        binding.resetConstructor.setOnClickListener {
            ResetLevelDialog.open()
        }

        binding.saveLevel.setOnClickListener {
            if(levelAdapter.isEmptyLevel()) {
                Toast.makeText(requireContext(), "Level is empty", Toast.LENGTH_SHORT).show()
            } else {
                SaveLevelDialog.open()
            }
        }

        binding.filterToggleButton.setOnClickListener {
            handleFilters()
        }

        binding.toCheckedEmoji.setOnClickListener {
            binding.allEmojisConstructor.smoothScrollToPosition(allEmojisAdapter.getCurrentElement())
        }

        binding.applyFilters.setOnClickListener {
            setCheckedFilters()
            handleFilters()
        }
        binding.resetFilters.setOnClickListener {
            allEmojisAdapter.resetFilters()
            handleFilters()
        }
    }

    private fun setCheckedFilters() {
        val categories : MutableList<String> = mutableListOf()
        for(chip in binding.categoriesChipGroup.children){
            if((chip as Chip).isChecked){
                categories.add(chip.text.toString())
            }
            allEmojisAdapter.filter(categories)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as DaggerAppCompatActivity).menuInflater.inflate(R.menu.constructor_menu, menu)
        saveItemIcon = menu.findItem(R.id.is_saved)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.is_saved -> {

                true
            }
            R.id.plus -> {
                scaleItems(1.0f,1.0f)
                true
            }
            R.id.minus -> {
                scaleItems(0.8f,0.8f)
                true
            }
            R.id.gridOnOff -> {
                changeGridStyle()
                true
            }
            R.id.sent_level -> {
                SentLevelDialog.open()
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    private fun handleHomeButton() {
        SaveLevelDialog.getSmallLevelModel()?.let {
            viewModel.hasDifferences(levelAdapter.currentList,
                it
            )
        }
//        viewModel.isSimilarList.observe(viewLifecycleOwner, Observer {
//            if(levelAdapter.currentList.isEmpty()){
//                this.findNavController().navigateUp()
//            } else if(it){
//                ExitLevelDialog.open()
//            } else if(!it) {
//                this.findNavController().navigateUp()
//            }
//        })
        if(!levelAdapter.isEmptyLevel()){
            ExitLevelDialog.open()
        } else {
            this.findNavController().navigateUp()
        }
    }

    private fun scaleItems(x : Float, y : Float) {
        binding.constructorLevelRv.animate().scaleX(x).scaleY(y)
        // Gets the layout params that will allow you to resize the layout
        val params  = binding.constructorLevelRv.layoutParams
        // Changes the height and width to the specified *pixels*
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.constructorLevelRv.layoutParams = params
        binding.constructorLevelRv.requestLayout()
    }

    private fun changeGridStyle() {
        isGridActive = if(isGridActive){
            setEmojiBackground(R.color.main_color)
            false
        } else {
            setEmojiBackground(R.drawable.stroke_fake)
            true
        }
    }

    private fun resetEmojis() {
        for(i : Int in 0..99){
            val holder = binding.constructorLevelRv
                .findViewHolderForAdapterPosition(i)
            holder?.itemView?.emoji_constructor_btn?.text =""
        }
        levelAdapter.resetOrder()
    }

    private fun setEmojiBackground(background: Int) {
        for(i : Int in 0..99){
            val holder = binding.constructorLevelRv
                .findViewHolderForAdapterPosition(i)
            holder?.itemView?.emoji_constructor_btn?.background =
                ContextCompat.getDrawable(requireContext(), background)
        }
    }

    private fun initConstructorAdapter() {
        levelAdapter = LevelConstructorRecyclerViewAdapter(LevelConstructorRecyclerViewAdapter
            .OnEmojiClickListener{
        })
        binding.constructorLevelRv.adapter = levelAdapter
    }

    private fun initAllEmojisAdapter() {
        allEmojisAdapter = AllEmojisRecyclerViewAdapter(AllEmojisRecyclerViewAdapter.OnEmojiClickListener {
                emojiShopModel: EmojiShopModel?, id : Int ->
            allEmojisAdapter.resetBackground(binding.allEmojisConstructor.findViewHolderForAdapterPosition(id))
            levelAdapter.setActiveElement(emojiShopModel)
        }, binding.levelProgressBar)
        binding.allEmojisConstructor.adapter = allEmojisAdapter
    }

    private fun getAllEmojisFromJson() {
        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.levelProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.levelProgressBar.visibility = View.INVISIBLE
                        allEmojisAdapter.allEmojisSubmitList(it.data)
                        generateGroupChips(it.data)
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun getLevelEmojis() {
        viewModel.constructorLevelResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        levelAdapter.submitList(it.data)
                    }
                }
            }
        })
    }

    private fun generateGroupChips(data: List<EmojiShopModel?>) {
        binding.categoriesChipGroup.removeAllViews()
        val groups = data.distinctBy {
            it?.group
        }
        for(model in groups){
            val chip = Chip(requireContext())
            chip.text = model?.group
            chip.isChipIconVisible = true
            chip.isClickable = true
            chip.isCheckable = true

            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun handleFilters() {
        isFilterVisible = if(!isFilterVisible){
            openFilters(binding.allEmojisConstructor, binding.filtersPlace, 180,300)
            binding.filterToggleButton
                .setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.icons8_filter_100px_close))
            true
        } else {
            closeFilters(binding.allEmojisConstructor, binding.filtersPlace,180,300)
            binding.filterToggleButton
                .setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.icons8_filter_100px))
            false
        }
    }

    private fun setBackButton() {
        val activity = ((activity) as AppCompatActivity)
        activity.setSupportActionBar(binding.constructorToolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        activity.supportActionBar?.title = ""

        binding.constructorToolbar.setNavigationOnClickListener {
            handleHomeButton()
        }
    }

}
