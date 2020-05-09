package com.example.emojifinder.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emojifinder.domain.result.Result
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.databinding.FragmentAccountBinding
import com.example.emojifinder.domain.viewModels.AccountViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AccountFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    lateinit var binding : FragmentAccountBinding
    lateinit var profile : MainAccountModel
    lateinit var adapter: UserLevelRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        // Inflate the layout for this fragment

        viewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = this

        setBackButton()
        initUserLevelsAdapter()
        observeLevelsStatistic()
        loadUserMainInfo()
        initButtons()

        return binding.root
    }

    private fun initButtons() {
        binding.profilePlace.setOnClickListener {
            this.findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToMainAccountInfoFragment(profile))
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
                        binding.levelDivider.visibility = View.VISIBLE
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
                            adapter.submitList(it.data)
                        } else {
                            binding.emptyPlace.visibility = View.VISIBLE
                            binding.errorTextAccount.text = resources.getString(R.string.empty_levels)
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

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAccount)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAccount.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
