package com.example.emojifinder.ui.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.ViewModelFactory
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentRatingBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.RatingViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RatingFragment : DaggerFragment() {

    private lateinit var binding : FragmentRatingBinding
    private lateinit var adapter : RatingRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory : ViewModelFactory
    lateinit var viewModel : RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatingBinding.inflate(inflater)

        setBackButton()
        initUsers()
        fetchUsers()

        addListenerToAdView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun fetchUsers() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.ratingProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.ratingProgressBar.visibility = View.INVISIBLE
                        adapter.submitList(it.data)
                    }
                    is Result.Error -> {
                        println(it.exception.message)
                    }
                }
            }
        })
    }

    private fun addListenerToAdView() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun initUsers() {
        adapter = RatingRecyclerViewAdapter()
        binding.ratingRv.adapter = adapter
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAccount)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAccount.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
