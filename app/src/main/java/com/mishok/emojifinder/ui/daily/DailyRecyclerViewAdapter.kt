package com.mishok.emojifinder.ui.daily

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.mishok.emojifinder.data.db.local.models.DailyModel

class DailyRecyclerViewAdapter(day: Int = 1)
    : AsyncListDifferDelegationAdapter<DailyModel>(DailyDiffCallback()) {

    init {
        delegatesManager.addDelegate(DailyItemAdapterDelegate(day))
    }

}