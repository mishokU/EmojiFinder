package com.mishok.emojifinder.ui.rating

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.mishok.emojifinder.core.di.utils.ViewModelFactory
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentRatingBinding
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.RatingViewModel
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject


class RatingFragment : DaggerFragment() {

    private lateinit var binding: FragmentRatingBinding
    private lateinit var adapter: RatingRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingBinding.inflate(inflater)

        initUsers()
        fetchUsers()
        initInvite()

        return binding.root
    }

    private fun initInvite() {
        binding.inviteBtn.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.mishok.emojifinder")
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
        adapter = RatingRecyclerViewAdapter(requireContext())
        binding.ratingRv.adapter = adapter
    }
}
