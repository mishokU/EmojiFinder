package com.mishok.emojifinder.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.databinding.FragmentAccountBinding
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import com.mishok.emojifinder.ui.account.user_emojis.UserEmojisAdapter
import com.mishok.emojifinder.ui.main.MainActivity
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

    private lateinit var userEmojisAdapter: UserEmojisAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = this

        binding.profilePlace.visibility = View.INVISIBLE

        UpdateAvatarDialog.create(this)

        loadUserMainInfo()

        initUserValuesViewModel()
        initValues()

        initUserLevelsAdapter()

        observeLevelsStatistic()

        initButtons()

        initListState()

        addListenerToAdView()

        initUpdateAvatar()

        return binding.root
    }

    private fun initUpdateAvatar() {
        UpdateAvatarDialog.getUpdateAvatarBtn().setOnClickListener {
            viewModel.updateUserAvatar(UpdateAvatarDialog.getEmoji())
            viewModel.fetchMainUserData()
            Toast.makeText(requireContext(), resources.getString(R.string.avatar_updated), Toast.LENGTH_SHORT).show()
            UpdateAvatarDialog.dialogView.dismiss()
        }
    }

    private fun initUserEmojisAdapter() {
        userEmojisAdapter = UserEmojisAdapter(profile.avatar) {
            UpdateAvatarDialog.show(it.text)
        }
        binding.userEmojisPlaceRv.adapter = userEmojisAdapter
    }

    private fun initValues() {
        binding.emojiBoxEt.setText(resources.getString(R.string.emoji_ticket))
        binding.emosEt.setText(resources.getString(R.string.emoji_emos))
        binding.emojiEt.setText(resources.getString(R.string.simple_emoji))
        binding.scoreEmoji.setText(resources.getString(R.string.emoji_score))
    }

    private fun addListenerToAdView() {
        if(!(activity as MainActivity).isVipAccount){
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }

    private fun initListState() {
        binding.startPlayBtn.setOnClickListener {
            this.findNavController().navigate(R.id.categotyGameFragment)
        }
    }

    private fun initUserValuesViewModel() {
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
            UserLevelRecyclerViewAdapter.OnLevelClickListener {

            },
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
                        profile = it.data
                        binding.profile = it.data

                        initUserEmojisAdapter()
                        initUserEmojisViewModel()
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
        viewModel.userEmojisResponse.observe(viewLifecycleOwner, {
            it?.let { userEmojis ->
                when (userEmojis) {
                    is Result.Loading -> {
                        binding.userEmojisProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.userEmojisProgressBar.visibility = View.INVISIBLE
                        userEmojisAdapter.items = userEmojis.data
                        initUserValuesViewModel()
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
        viewModel.fetchUserLevelsStatistic()
        viewModel.levelsStatisticResponse.observe(viewLifecycleOwner, {
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
