package com.mishok.emojifinder.ui.daily

import androidx.recyclerview.widget.DiffUtil
import com.mishok.emojifinder.data.db.local.models.DailyModel

class DailyDiffCallback : DiffUtil.ItemCallback<DailyModel>() {

    override fun areItemsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
        return oldItem.id ==newItem.id
    }

    override fun areContentsTheSame(oldItem: DailyModel, newItem: DailyModel): Boolean {
        return oldItem == newItem
    }
}
