package com.mishok.emojifinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentMainMenuBinding
import com.mishok.emojifinder.domain.notifications.NotificationsService
import com.mishok.emojifinder.domain.prefs.NotificationAlarmPrefs
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MainMenuFragment : DaggerFragment() {

    lateinit var binding: FragmentMainMenuBinding

    @Inject
    lateinit var alarmPrefs: NotificationAlarmPrefs

    @Inject
    lateinit var notificationsService: NotificationsService

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainMenuBinding.inflate(inflater)

        handleButtons()
        setNotifications()
        loadUserAvatar()

        return binding.root
    }

    private fun loadUserAvatar() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.userMainDataResponse.observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loadingAvatar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        hideProfileLoading()
                        binding.profileEmoji.text = it.data.avatar
                        addListenerToAdView(it.data.vip)
                    }
                    is Result.Error -> {
                        hideProfileLoading()
                        binding.profileEmoji.text = resources.getString(R.string.simple_emoji)
                    }
                }
            }
        })
    }

    private fun hideProfileLoading() {
        binding.loadingAvatar.visibility = View.GONE
        binding.loadingAvatar.cancelAnimation()
    }

    private fun setNotifications() {
//        if (!alarmPrefs.isStarted()) {
//            notificationsService.create()
//        } else {
//            alarmPrefs.setStarted()
//        }
//        notificationsService.create()
    }

    private fun addListenerToAdView(showAd : Boolean) {
        if(!showAd){
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
            binding.adView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    binding.adView.loadAd(AdRequest.Builder().build())
                }
            }
        }
    }

    private fun handleButtons() {
        binding.playLevel.setOnClickListener {
            this.findNavController().navigate(R.id.arcadeGameFragment)
        }

        binding.startArcadeGameBtn.setOnClickListener {
            this.findNavController().navigate(R.id.arcadeGameFragment)
        }

        binding.startCategoryGameBtn.setOnClickListener {
            this.findNavController().navigate(R.id.categotyGameFragment)
        }
        binding.profileMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.accountFragment)
        }
        binding.settingsMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.settingsFragment)
        }

        binding.shopMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.accountAvatarFragment)
        }

        binding.shopBtn.setOnClickListener {
            //Toast.makeText(requireContext(),getString(R.string.next_version), Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(R.id.shopFragment)
        }
    }
}
