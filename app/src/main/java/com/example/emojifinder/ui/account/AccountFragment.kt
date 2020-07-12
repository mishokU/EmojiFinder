package com.example.emojifinder.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.databinding.FragmentAccountBinding
import com.example.emojifinder.domain.adds.BANNER_ID
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.ui.shop.EmojisRecyclerViewAdapter
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    var login: String = ""

    lateinit var binding: FragmentAccountBinding
    lateinit var profile: MainAccountModel
    lateinit var adapter: UserLevelRecyclerViewAdapter

    lateinit var userAdapter: EmojisRecyclerViewAdapter
    lateinit var userEmojisAdapter: EmojisRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = this

        binding.profilePlace.visibility = View.INVISIBLE

        initUserValuesViewModel()
        initValues()
        initUserLevelsAdapter()
        observeLevelsStatistic()
        loadUserMainInfo()
        initButtons()

        initListState()
        initUserEmojisAdapter()

        initUserEmojisViewModel()

        addListenerToAdView()

        return binding.root
    }

    private fun initUserEmojisAdapter() {
        userEmojisAdapter = EmojisRecyclerViewAdapter(
            EmojisRecyclerViewAdapter.OnShopItemClickListener {},
            binding.userEmojisProgressBar, false
        )
        binding.userEmojisPlaceRv.adapter = userEmojisAdapter
    }

    private fun initValues() {
        binding.emojiBoxEt.setText("\uD83C\uDF9F️")
        binding.emosEt.setText("\uD83D\uDCB0")
        binding.emojiEt.setText("\uD83D\uDE00")
        binding.scoreEmoji.setText("\uD83C\uDFAF")
    }

    private fun addListenerToAdView() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initListState() {
        binding.startPlayBtn.setOnClickListener {
            this.findNavController().navigate(R.id.categotyGameFragment)
        }
    }

    private fun initUserValuesViewModel() {
        viewModel.fetchUserValues()
        viewModel.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { values ->
                when (values) {
                    is Result.Success -> {
                        binding.emosCount.text = values.data.emos.toString()
                        binding.boxesCount.text = values.data.boxes.toString()
                        binding.emojisCount.text = values.data.emojis.toString()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchMainUserData()
    }

    private fun initButtons() {
        binding.profilePlace.setOnClickListener {
            this.findNavController().navigate(
                AccountFragmentDirections
                    .actionAccountFragmentToMainAccountInfoFragment(profile)
            )
        }

        binding.ratingBtn.setOnClickListener {
            this.findNavController().navigate(R.id.ratingFragment)
        }

    }

    private fun initUserLevelsAdapter() {
        adapter = UserLevelRecyclerViewAdapter(
            UserLevelRecyclerViewAdapter.OnLevelClickListener {},
            requireContext()
        )
        binding.levelsRecyclerView.adapter = adapter
    }

    private fun loadUserMainInfo() {
        viewModel.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loadingProfile.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingProfile.visibility = View.GONE
                        binding.profilePlace.visibility = View.VISIBLE
                        profile = it.data!!
                        binding.profile = it.data
                    }
                    is Result.Error -> {
                        binding.loadingProfile.visibility = View.GONE
                        binding.profileTextError.text = it.exception.message
                    }
                }
            }
        })
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
                        //userEmojisCount = userEmojis.data.size.toString()

                        initUserValuesViewModel()
                        //initShopViewModel(userEmojis.data)
                    }
                    is Result.Error -> {
                        binding.userEmojisProgressBar.visibility = View.INVISIBLE
                        binding.errorMessage.text = userEmojis.exception.message
                    }
                }
            }
        })
    }

    private fun observeLevelsStatistic() {
        viewModel.levelsStatisticResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loadingLevelsAccount.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingLevelsAccount.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            adapter.submitList(it.data)
                        } else {
                            binding.emptyPlace.visibility = View.VISIBLE
                            binding.errorTextAccount.text =
                                resources.getString(R.string.empty_levels)
                        }
                    }
                    is Result.Error -> {
                        binding.loadingLevelsAccount.visibility = View.GONE
                        binding.errorTextAccount.visibility = View.VISIBLE
                        binding.errorTextAccount.text = it.exception.message
                    }
                }
            }
        })
    }
}
