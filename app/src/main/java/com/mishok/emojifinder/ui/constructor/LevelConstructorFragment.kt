package com.mishok.emojifinder.ui.constructor

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.ViewModelFactory
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentLevelConstructorBinding
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.ConstructorViewModel
import com.mishok.emojifinder.domain.viewModels.SharedViewModel
import com.mishok.emojifinder.ui.base.BaseImageFragment
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import com.mishok.emojifinder.ui.constructor.choosePhoto.PhotoPickerBottomDialogFragment
import com.mishok.emojifinder.ui.constructor.dialogs.ExitLevelDialog
import com.mishok.emojifinder.ui.constructor.dialogs.ResetLevelDialog
import com.mishok.emojifinder.ui.constructor.dialogs.SaveLevelDialog
import com.mishok.emojifinder.ui.constructor.dialogs.SentLevelDialog
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import com.mishok.emojifinder.ui.utils.closeFilters
import com.mishok.emojifinder.ui.utils.openFilters
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.emoji_constructor_item.view.*
import javax.inject.Inject


class LevelConstructorFragment : BaseImageFragment() {

    private lateinit var binding: FragmentLevelConstructorBinding
    private lateinit var allEmojisAdapter: AllEmojisRecyclerViewAdapter
    private lateinit var levelAdapter: LevelConstructorRecyclerViewAdapter

    private var isGridActive: Boolean = true
    private var isFilterVisible: Boolean = false

    private val model: SharedViewModel by activityViewModels()

    private lateinit var level: SmallLevelModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ConstructorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        initButtons()
        initSameTitle()

        getAllEmojisFromJson()
        getLevel()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun initSameTitle() {
        viewModel.hasTitleResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    SentLevelDialog.getNameLabel().error =
                        resources.getString(R.string.level_name_should_be_unique)
                    viewModel.hasTitleComplete()
                } else {
                    viewModel.sentLevel(
                        levelAdapter.getItems(),
                        SentLevelDialog.getSmallLevelModel(),
                        SentLevelDialog.getImage()
                    )
                    this.findNavController().navigateUp()
                    SentLevelDialog.dialogView.dismiss()
                    viewModel.hasTitleComplete()
                }
            }
        })
    }

    private fun getLevel() {
        if (requireArguments().containsKey("Level")) {
            level = LevelConstructorFragmentArgs.fromBundle(requireArguments()).Level
            SaveLevelDialog.setLevel(level)
            SentLevelDialog.setLevel(level)
            fetchLevel(level)
        } else {
            getLevelEmojis()
        }
    }

    private fun fetchLevel(level: SmallLevelModel) {
        viewModel.levelTitle = level.title
        viewModel.emojis.observe(viewLifecycleOwner, Observer {
            levelAdapter.setData(it)
            levelAdapter.setOrder()
        })
    }

    private fun initDialogButtons() {
        ResetLevelDialog.getResetLevelBtn().setOnClickListener {
            resetEmojis()
            ResetLevelDialog.dialogView.dismiss()
        }

        SentLevelDialog.getLevelPicture().setOnClickListener {
            val photoPicker = PhotoPickerBottomDialogFragment()
            activity?.supportFragmentManager?.let {
                    it1 -> photoPicker.show(it1,"photos")
            }
        }

        model.photo.observe(viewLifecycleOwner, Observer {
            SentLevelDialog.setImage(it?.uri)
        })

        SaveLevelDialog.getSaveLevelBtn().setOnClickListener {
            if (SaveLevelDialog.isNotEmpty()) {
                if (SaveLevelDialog.getSmallLevelModel() != null) {
                    levelAdapter.setLevelTitleToEmojis(SaveLevelDialog.getNameLabel().text.toString())
                    viewModel.saveLevel(
                        levelAdapter.getItems(),
                        SaveLevelDialog.getSmallLevelModel()!!
                    )
                    SaveLevelDialog.dialogView.dismiss()
                }
                this.findNavController().navigateUp()
            }
        }

        SentLevelDialog.getSentLevelBtn().setOnClickListener {
            if (SentLevelDialog.isNotEmpty()) {
                viewModel.checkOnSameTitle(SentLevelDialog.getSmallLevelModel()!!.title)
            }
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
            if (levelAdapter.isEmptyLevel()) {
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
        val categories: MutableList<String> = mutableListOf()
        for (chip in binding.categoriesChipGroup.children) {
            if ((chip as Chip).isChecked) {
                categories.add(chip.text.toString())
            }
            allEmojisAdapter.filter(categories)
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as DaggerAppCompatActivity).menuInflater.inflate(R.menu.constructor_menu, menu)
        for (item in menu.children) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                item.iconTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.buttonBlueColor)
            } else {
                val iconDrawable = item.icon
                iconDrawable.mutate()
                iconDrawable.setColorFilter(
                    resources.getColor(R.color.buttonBlueColor),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.is_saved -> {

                true
            }
            R.id.plus -> {
                scaleItems(1.0f, 1.0f)
                true
            }
            R.id.minus -> {
                scaleItems(0.8f, 0.8f)
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
            viewModel.hasDifferences(
                levelAdapter.getItems(),
                it
            )
        }
        if (!levelAdapter.isEmptyLevel()) {
            ExitLevelDialog.open()
        } else {
            this.findNavController().navigateUp()
        }
    }

    private fun scaleItems(x: Float, y: Float) {
        binding.constructorLevelRv.animate().scaleX(x).scaleY(y)
        // Gets the layout params that will allow you to resize the layout
        val params = binding.constructorLevelRv.layoutParams
        // Changes the height and width to the specified *pixels*
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.constructorLevelRv.layoutParams = params
        binding.constructorLevelRv.requestLayout()
    }

    private fun changeGridStyle() {
        isGridActive = if (isGridActive) {
            setEmojiBackground(null)
            false
        } else {
            setEmojiBackground(R.drawable.stroke_fake)
            true
        }
    }

    private fun resetEmojis() {
        for (i: Int in 0..99) {
            val holder = binding.constructorLevelRv
                .findViewHolderForAdapterPosition(i)
            (holder as LevelConstructorRecyclerViewAdapter.KeyboardViewHolder).itemView.emoji_constructor_btn?.text = ""
        }
        levelAdapter.resetOrder()
    }

    private fun setEmojiBackground(background: Int?) {
        for (i: Int in 0..99) {
            val holder = binding.constructorLevelRv
                .findViewHolderForAdapterPosition(i)
            if (background != null) {
                holder?.itemView?.emoji_constructor_btn?.background =
                    ContextCompat.getDrawable(requireContext(), background)
            } else {
                holder?.itemView?.emoji_constructor_btn?.background = null
            }
        }
    }

    private fun initConstructorAdapter() {
        levelAdapter = LevelConstructorRecyclerViewAdapter(LevelConstructorRecyclerViewAdapter
            .OnEmojiClickListener {
            })
        binding.constructorLevelRv.adapter = levelAdapter
    }

    private fun initAllEmojisAdapter() {
        allEmojisAdapter =
            AllEmojisRecyclerViewAdapter(AllEmojisRecyclerViewAdapter.OnEmojiClickListener { emojiShopModel: EmojiShopModel?, id: Int ->
                allEmojisAdapter.resetBackground(
                    binding.allEmojisConstructor.findViewHolderForAdapterPosition(
                        id
                    )
                )
                levelAdapter.setActiveElement(emojiShopModel)
            }, binding.levelProgressBar)
        binding.allEmojisConstructor.adapter = allEmojisAdapter
    }

    private fun getAllEmojisFromJson() {
        viewModel.randomEmojis.apply {
            allEmojisAdapter.allEmojisSubmitList(this)
            generateGroupChips(this)
        }
    }

    private fun getLevelEmojis() {
        viewModel.constructorLevelResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        levelAdapter.setData(it.data)
                    }
                }
            }
        })
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
            chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.ru_eng_sriracha)
            chip.isChipIconVisible = true
            chip.isClickable = true
            chip.isCheckable = true
            chip.setTextColor(resources.getColor(R.color.background_color))
            chip.chipIconTint = resources.getColorStateList(R.color.background_color)
            chip.chipBackgroundColor = resources.getColorStateList(R.color.blue_color)
            binding.categoriesChipGroup.addView(chip)
        }
    }

    private fun handleFilters() {
        isFilterVisible = if (!isFilterVisible) {
            openFilters(binding.allEmojisConstructor, binding.filtersPlace, 180, 300)
            binding.filterToggleButton
                .setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icons8_filter_100px_close
                    )
                )
            true
        } else {
            closeFilters(binding.allEmojisConstructor, binding.filtersPlace, 180, 300)
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
