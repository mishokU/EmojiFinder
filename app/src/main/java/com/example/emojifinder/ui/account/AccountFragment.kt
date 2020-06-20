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
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    var login : String = ""

    lateinit var binding : FragmentAccountBinding
    lateinit var profile : MainAccountModel
    lateinit var adapter: UserLevelRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = this

        binding.profilePlace.visibility = View.INVISIBLE

        setBackButton()
        initUserLevelsAdapter()
        observeLevelsStatistic()
        loadUserMainInfo()
        initButtons()

        initListState()

        addListenerToAdView()

        return binding.root
    }

    private fun addListenerToAdView() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initListState() {
        binding.levelStateSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                adapter.collapseAll()
                //binding.levelsRecyclerView.getVi
            } else {
                adapter.expandAll()
            }
        }

        binding.startPlayBtn.setOnClickListener {
            this.findNavController().navigate(R.id.categotyGameFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchMainUserData()
    }

    private fun initButtons() {
        binding.profilePlace.setOnClickListener {
            this.findNavController().navigate(AccountFragmentDirections
                .actionAccountFragmentToMainAccountInfoFragment(profile))
        }

        binding.ratingBtn.setOnClickListener {
            this.findNavController().navigate(R.id.ratingFragment)
        }

    }

    private fun initUserLevelsAdapter() {
        adapter = UserLevelRecyclerViewAdapter(UserLevelRecyclerViewAdapter.OnLevelClickListener {},
            requireContext())
        binding.levelsRecyclerView.adapter = adapter
    }

    private fun loadUserMainInfo() {
        viewModel.userMainDataResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
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

    private fun observeLevelsStatistic() {
        viewModel.levelsStatisticResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.loadingLevelsAccount.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingLevelsAccount.visibility = View.GONE
                        if(!it.data.isNullOrEmpty()){
                            binding.levelDivider.visibility = View.VISIBLE

                            adapter.submitList(it.data)
                        } else {
                            binding.levelDivider.visibility = View.GONE
                            binding.emptyPlace.visibility = View.VISIBLE
                            binding.errorTextAccount.text = resources.getString(R.string.empty_levels)
                        }
                    }
                    is Result.Error -> {
                        binding.levelDivider.visibility = View.GONE
                        binding.loadingLevelsAccount.visibility = View.GONE
                        binding.errorTextAccount.visibility = View.VISIBLE
                        binding.errorTextAccount.text = it.exception.message
                    }
                }
            }
        })
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAccount)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAccount.setNavigationOnClickListener {
            viewModel.statisticResponseComplete()
            this.findNavController().navigateUp()
        }
    }

}
