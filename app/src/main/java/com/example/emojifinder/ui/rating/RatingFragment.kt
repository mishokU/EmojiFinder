package com.example.emojifinder.ui.rating

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.emojifinder.core.di.utils.ViewModelFactory
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentRatingBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.RatingViewModel
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject


class RatingFragment : DaggerFragment() {

    private val TAG = "Rating Fragment";
    private lateinit var binding: FragmentRatingBinding
    private lateinit var adapter: RatingRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatingBinding.inflate(inflater)

        initUsers()
        fetchUsers()
        initInvite();

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initInvite() {
        binding.inviteBtn.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun fetchUsers() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.ratingProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.ratingProgressBar.visibility = View.INVISIBLE
                        adapter.submitList(it.data)
                    }
                    is Result.Error -> {
                        Timber.e(it.exception)
                    }
                }
            }
        })
    }

    private fun initUsers() {
        adapter = RatingRecyclerViewAdapter()
        binding.ratingRv.adapter = adapter
    }
}
