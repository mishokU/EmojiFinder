package com.example.emojifinder.domain.adds

import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener

abstract class MyRewardedVideoListener : RewardedVideoAdListener {
    override fun onRewarded(p0: RewardItem?) {}
    override fun onRewardedVideoAdClosed() {}
    override fun onRewardedVideoAdLeftApplication() {}
    override fun onRewardedVideoAdLoaded() {}
    override fun onRewardedVideoAdOpened() {}
    override fun onRewardedVideoCompleted() {}
    override fun onRewardedVideoStarted() {}
    override fun onRewardedVideoAdFailedToLoad(p0: Int) {}
}